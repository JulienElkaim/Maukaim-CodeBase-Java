package com.maukaim.org.utils.diffable.utils;

import com.maukaim.org.utils.serializer.utils.JsonHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.IntStream;

@Slf4j
public class DiffUtils {
    public static final List<Class<?>> STANDARD_FIELD_TYPE = List.of(
            char.class,
            int.class,
            byte.class,
            long.class,
            float.class,
            short.class,
            double.class,
            boolean.class,
            String.class,
            char[].class,
            int[].class,
            byte[].class,
            long[].class,
            float[].class,
            short[].class,
            double[].class,
            boolean[].class
    );

    public static final String DEFAULT_NO_VALUE_MESSAGE = "[No Value]";
    private static final ToStringStyle DEFAULT_TOSTRING_STYLE = ToStringStyle.JSON_STYLE;

    private static <T> Set<Field> getAllFields(Class<T> objClass) {
        return ReflectionUtils.getAllFields(objClass, input -> !Objects.requireNonNull(input)
                .isAnnotationPresent(DiffableIgnore.class));
    }

    public static <T> DiffResult getGenericDiff(Class<T> objClass, T obj1, T obj2) {
        DiffBuilder diffBuilder = new DiffBuilder(obj1, obj2, DEFAULT_TOSTRING_STYLE);

        Set<Field> allObjectTypeExistingFields = getAllFields(objClass);

        for (Field field : allObjectTypeExistingFields) {
            if (Collection.class.isAssignableFrom(field.getType())) {
                getGenericCollectionDiff(diffBuilder, field, obj1, obj2);
            } else {
                String fieldName = field.getName();
                Object defaultingValue = new Object();
                Object obj1FieldValue = getFieldValue(field, obj1).orElse(defaultingValue);
                Object obj2FieldValue = getFieldValue(field, obj2).orElse(defaultingValue);
                getGenericNotCollectionDiff(diffBuilder, fieldName, field.getClass(), obj1FieldValue, obj2FieldValue);
            }
        }

        return diffBuilder.build();
    }

    private static <T> void getGenericCollectionDiff(DiffBuilder diffBuilder, Field field, T obj1, T obj2) {
        Collection<Object> obj1FieldValue = (Collection<Object>) getFieldValue(field, obj1).orElseGet(() -> Collections.emptyList());
        Collection<Object> obj2FieldValue = (Collection<Object>) getFieldValue(field, obj2).orElseGet(() -> Collections.emptyList());

        if (Objects.isNull(obj1FieldValue) || Objects.isNull(obj2FieldValue)) {
            if (obj1FieldValue != null) {
                diffBuilder.append(
                        String.format("%s.(deletion)",
                                field.getName()),
                        JsonHelper.serialize(obj1FieldValue),
                        DEFAULT_NO_VALUE_MESSAGE);
            }
            if (obj2FieldValue != null) {
                diffBuilder.append(
                        String.format("%s.(creation)",
                                field.getName()),
                        DEFAULT_NO_VALUE_MESSAGE,
                        JsonHelper.serialize(obj2FieldValue));
            }
        } else {
            if (obj1FieldValue.size() == obj2FieldValue.size()) {
                Object[] obj1FieldValueArray = obj1FieldValue.toArray();
                Object[] obj2FieldValueArray = obj2FieldValue.toArray();

                ParameterizedType stringListType = (ParameterizedType) field.getGenericType();
                Class subClassOfCollection = (Class) stringListType.getActualTypeArguments()[0];
                boolean isDiffable = Diffable.class.isAssignableFrom(subClassOfCollection);
                IntStream.range(0, obj1FieldValueArray.length)
                        .forEach(index -> {
                            if (isDiffable) {
                                diffBuilder.append(
                                        String.format("%s.#%s", field.getName(), index + 1),
                                        ((Diffable) obj1FieldValueArray[index]).diff(obj2FieldValueArray[index]));
                            } else {
                                diffBuilder.append(String.format("%s.#%s", field.getName(), index + 1),
                                        obj1FieldValueArray[index], obj2FieldValueArray[index]);
                            }
                        });
            } else {
                String pluralMarker = Math.abs(obj1FieldValue.size() - obj2FieldValue.size()) > 1 ? "s" : "";
                String actionPerformed = obj1FieldValue.size() > obj2FieldValue.size() ?
                        String.format("Removed %s element%s", obj1FieldValue.size() - obj2FieldValue.size(), pluralMarker) :
                        String.format("Added %s element%s", obj2FieldValue.size() - obj1FieldValue.size(), pluralMarker);
                diffBuilder.append(
                        String.format("%s.%s.Size %s -> %s",
                                field.getName(),
                                actionPerformed,
                                obj1FieldValue.size(),
                                obj2FieldValue.size()),
                        JsonHelper.serialize(obj1FieldValue),
                        JsonHelper.serialize(obj2FieldValue));
            }
        }
    }

    private static <T> void getGenericNotCollectionDiff(DiffBuilder diffBuilder, String fieldName, Class<?> clazz, T obj1FieldValue, T obj2FieldValue) {
        if(Diffable.class.isAssignableFrom(clazz)){
            Diffable<Object> diffable = (Diffable<Object>) obj1FieldValue;
            diffBuilder.append(fieldName, diffable.diff(obj2FieldValue));
        } else if(DiffUtils.STANDARD_FIELD_TYPE.contains(clazz)){
            //IDEA: Maybe differentiate getFieldValue according to type matched in the previous condition ?
            diffBuilder.append(fieldName, obj1FieldValue, obj2FieldValue);
        }else{
            //IDEA: Maybe compare with more specialized stuff to add to the builder? Now just leveraging Object comparison of DiffBuilder
            diffBuilder.append(fieldName, obj1FieldValue, obj2FieldValue);
        }
    }

    private static <T> Optional<Object> getFieldValue(Field objField, T obj) {
        try{
            return Optional.ofNullable(objField.get(obj));
        }catch (IllegalAccessException e){
            String potentialGetterMethodName = mockAccessorName(objField);
            for(Method method: obj.getClass().getMethods()){
                if(method.getName().equalsIgnoreCase(potentialGetterMethodName)){
                    try{
                        return Optional.ofNullable(method.invoke(obj));
                    } catch (IllegalAccessException ex){
                        throw new RuntimeException(String.format("Cant invok method %s on Object class %s, instance -> %s",
                                method.getName(),
                                obj.getClass().getSimpleName(),
                                obj.toString()));
                    } catch (InvocationTargetException ex){
                        throw new RuntimeException(String.format("Invocation issue on method %s on object class %s, instance -> %s",
                                method.getName(),
                                obj.getClass().getSimpleName(),
                                obj.toString()));
                    }
                }
            }
            return Optional.empty();
        }
    }

    private static String mockAccessorName(Field objField){
        if(objField.getType() == boolean.class){
            return "is" + StringUtils.capitalize(objField.getName());
        }
        return "get" + StringUtils.capitalize(objField.getName());
    }
}
