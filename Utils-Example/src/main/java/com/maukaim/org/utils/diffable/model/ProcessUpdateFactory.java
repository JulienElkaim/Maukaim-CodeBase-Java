package com.maukaim.org.utils.diffable.model;

import com.maukaim.org.utils.audit.model.MkProcessAudit;
import com.maukaim.org.utils.domain.model.MkProcess;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.Diff;
import org.apache.commons.lang3.builder.DiffResult;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class ProcessUpdateFactory {

    public static List<ProcessUpdate> build(List<MkProcessAudit> processes){
        List<ProcessUpdate> processUpdates = new ArrayList<>();
        IntStream.range(0, processes.size()).forEach(
                index -> {
                    if(index > 0){
                        MkProcessAudit actualMkProcessAudit = processes.get(index);
                        MkProcess actualProcess = actualMkProcessAudit.getProcess();
                        MkProcess previousProcess = processes.get(index - 1).getProcess();
                        DiffResult<MkProcess> diffResult = previousProcess.diff(actualProcess);
                        List<ProcessUpdateItem> updateItems = diffResult.getDiffs().stream()
                                .map(ProcessUpdateFactory::build)
                                .collect(Collectors.toList());
                        ProcessUpdate update = ProcessUpdateFactory.build(actualMkProcessAudit, updateItems);
                        if(!CollectionUtils.isEmpty(update.getUpdateItems())){
                            processUpdates.add(update);
                        }
                    }
                }
        );
        return processUpdates;
    }

    private static ProcessUpdate build(MkProcessAudit actualMkProcessAudit, List<ProcessUpdateItem> updateItems){
        return ProcessUpdate.builder()
                .modificationTime(actualMkProcessAudit.getModifiedDate())
                .modifierName(actualMkProcessAudit.getModifiedBy())
                .actionType(actualMkProcessAudit.getAction())
                .updateItems(updateItems)
                .build();
    }

    private static ProcessUpdateItem build(Diff<?> diff){
        return ProcessUpdateItem.builder()
                .path(List.of(diff.getFieldName().split("\\.")))
                .oldValue(diff.getLeft().toString())
                .newValue(diff.getRight().toString())
                .build();
    }
}
