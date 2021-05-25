package com.maukaim.org.utils.domain.model.mock;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MockObject<O> {
    private String serializedValue;
    private O value;

    public static <O> MockObject<O> of(O value, String serializedValue){
        return MockObject.<O>builder()
                .serializedValue(serializedValue)
                .value(value)
                .build();
    }
}
