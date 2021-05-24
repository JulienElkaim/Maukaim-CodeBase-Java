package com.maukaim.org.utils.serializer.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.maukaim.org.utils.domain.model.Step;
import com.maukaim.org.utils.serializer.utils.JsonHelper;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import java.io.Serializable;
import java.util.List;

/**
 * Does not work in @Convert for the moment.
 * @param <O>
 */
@Slf4j
public abstract class ObjectListJsonConverter<O> implements AttributeConverter<List<O>, String> {
    @Override
    public String convertToDatabaseColumn(List<O> attribute) {
        return JsonHelper.serialize(attribute);
    }

    @Override
    public List<O> convertToEntityAttribute(String dbData) {
        return JsonHelper.deserialize(dbData, new TypeReference<>(){});
    }
}
