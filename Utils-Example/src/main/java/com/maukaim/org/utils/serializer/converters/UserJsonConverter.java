package com.maukaim.org.utils.serializer.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.maukaim.org.utils.domain.model.User;
import com.maukaim.org.utils.serializer.utils.JsonHelper;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;


@Slf4j
public class UserJsonConverter implements AttributeConverter<User, String> {
    @Override
    public String convertToDatabaseColumn(User attribute) {
        return JsonHelper.serialize(attribute);
    }

    @Override
    public User convertToEntityAttribute(String dbData) {
        return JsonHelper.deserialize(dbData, new TypeReference<>(){});
    }
}
