package com.maukaim.org.utils.diffable.model;

import com.maukaim.org.utils.audit.model.ProcessAudit;
import com.maukaim.org.utils.model.Process;
import org.apache.commons.lang3.builder.Diff;
import org.apache.commons.lang3.builder.DiffResult;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProcessUpdateFactory {

    private static List<ProcessUpdate> build(List<ProcessAudit> processes){
        List<ProcessUpdate> processUpdates = new ArrayList<>();
        IntStream.range(0, processUpdates.size()).forEach(
                index -> {
                    if(index > 0){
                        ProcessAudit actualProcessAudit = processes.get(index);
                        Process actualProcess = actualProcessAudit.getProcess();
                        Process previousProcess = processes.get(index - 1).getProcess();
                        DiffResult<Process> diffResult = previousProcess.diff(actualProcess);
                        List<ProcessUpdateItem> updateItems = diffResult.getDiffs().stream()
                                .map(ProcessUpdateFactory::build)
                                .collect(Collectors.toList());
                        ProcessUpdate update = ProcessUpdateFactory.build(actualProcessAudit, updateItems);
                        if(!CollectionUtils.isEmpty(update.getUpdateItems())){
                            processUpdates.add(update);
                        }

                    }
                }
        );
        return processUpdates;
    }

    private static ProcessUpdate build(ProcessAudit actualProcessAudit, List<ProcessUpdateItem> updateItems){
        return ProcessUpdate.builder()
                .modificationTime(actualProcessAudit.getModifiedDate())
                .modifierName(actualProcessAudit.getModifiedBy())
                .actionType(actualProcessAudit.getAction())
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
