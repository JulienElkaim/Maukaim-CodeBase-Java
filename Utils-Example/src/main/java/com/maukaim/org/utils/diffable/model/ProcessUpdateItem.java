package com.maukaim.org.utils.diffable.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProcessUpdateItem {
    private List<String> path;
    private String oldValue;
    private String newValue;
}
