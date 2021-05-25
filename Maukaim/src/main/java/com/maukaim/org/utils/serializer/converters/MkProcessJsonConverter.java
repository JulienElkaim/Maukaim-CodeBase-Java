package com.maukaim.org.utils.serializer.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.maukaim.org.utils.domain.model.MkProcess;
import com.maukaim.org.utils.serializer.utils.JsonHelper;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;


@Slf4j
public class MkProcessJsonConverter implements AttributeConverter<MkProcess, String> {
    @Override
    public String convertToDatabaseColumn(MkProcess attribute) {
        return JsonHelper.serialize(attribute);
    }

    @Override
    public MkProcess convertToEntityAttribute(String dbData) {
        return JsonHelper.deserialize(dbData, new TypeReference<>(){});
    }
}
