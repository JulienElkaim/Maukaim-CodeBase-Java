package com.maukaim.org.utils.serializer.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.maukaim.org.utils.domain.model.Step;
import com.maukaim.org.utils.serializer.utils.JsonHelper;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import java.io.Serializable;
import java.util.List;


@Slf4j
public class StepListJsonConverter implements AttributeConverter<List<Step>, String>{
    @Override
    public String convertToDatabaseColumn(List<Step> attribute) {
        return JsonHelper.serialize(attribute);
    }

    @Override
    public List<Step> convertToEntityAttribute(String dbData) {
        return JsonHelper.deserialize(dbData, new TypeReference<>(){});
    }
}
