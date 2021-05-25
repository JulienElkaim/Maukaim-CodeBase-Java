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
public class ProcessUpdate {
    private String modifierName;
    private LocalDateTime modificationTime;
    private String actionType;
    private List<ProcessUpdateItem> updateItems;
}
