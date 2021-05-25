package com.maukaim.org.utils.serializer.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.maukaim.org.utils.serializer.utils.JsonHelper;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Does not work in @Convert for the moment.
 * @param <E> Enum type converted
 * @best-practice How to use:
 * - In the Enum class create public class extending this abstract, generified to Enum class.
 * - In the convert just provide EnumClass.ClassExtendingThisAbstract.class
 */
@Slf4j
public abstract class AbstractEnumListJsonConverter<E extends Enum<E>> implements AttributeConverter<List<E>, String> {
    @Override
    public String convertToDatabaseColumn(List<E> attribute) {
        List<String> stringifiedEnums = attribute.stream().map(Enum::name).collect(Collectors.toList());
        return JsonHelper.serialize(stringifiedEnums);
    }

    @Override
    public List<E> convertToEntityAttribute(String dbData) {
        List<String> stringifiedEnumList = JsonHelper.deserialize(dbData, new TypeReference<>() {});
        if(Objects.nonNull(stringifiedEnumList)){
            return stringifiedEnumList.stream()
                    .map(stren-> Enum.valueOf((Class<E>) this.getEnumClass(), stren))
                    .collect(Collectors.toList());
        }
        else{
            throw new ConvertException("Deserialization of enum entity failed ! value returned is null");
        }
    }

    private Class<?> getEnumClass(){
        String typeName = getGenericName();

        try{
            return Class.forName(getGenericName());
        } catch (ClassNotFoundException e){
            String err = String.format(
                    "While converting entity to Enum %s, unable to get Class<E>. Error -> %s",
                    typeName, e);
            log.error(err);
            throw new ConvertException(err);
        }
    }

    public String getGenericName(){
        return ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0].getTypeName();
    }
}
