package com.maukaim.org.utils.serializer.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.maukaim.org.utils.serializer.utils.JsonHelper;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import java.io.Serializable;
import java.util.List;


/**
 * Does not work in @Convert for the moment.
 * @param <S>
 */
@Slf4j
public class SerializableListJsonConverter<S extends Serializable> implements AttributeConverter<List<S>, String> {
    @Override
    public String convertToDatabaseColumn(List<S> attribute) {
        return JsonHelper.serialize(attribute);
    }

    @Override
    public List<S> convertToEntityAttribute(String dbData) {
        return JsonHelper.deserialize(dbData, new TypeReference<>(){});
    }
}
