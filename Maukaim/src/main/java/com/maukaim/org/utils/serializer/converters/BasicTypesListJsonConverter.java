package com.maukaim.org.utils.serializer.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.maukaim.org.utils.serializer.utils.JsonHelper;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import java.io.Serializable;
import java.util.List;


@Slf4j
public class BasicTypesListJsonConverter<O extends Serializable> implements AttributeConverter<List<O>, String> {
    @Override
    public String convertToDatabaseColumn(List<O> attribute) {
        return JsonHelper.serialize(attribute);
    }

    @Override
    public List<O> convertToEntityAttribute(String dbData) {
        return JsonHelper.deserialize(dbData, new TypeReference<>(){});
    }
}
