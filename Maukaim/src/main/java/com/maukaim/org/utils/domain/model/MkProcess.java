package com.maukaim.org.utils.domain.model;

import com.maukaim.org.utils.audit.model.MkProcessAuditListener;
import com.maukaim.org.utils.diffable.utils.DiffUtils;
import com.maukaim.org.utils.serializer.converters.ObjectListJsonConverter;
import com.maukaim.org.utils.serializer.converters.StepListJsonConverter;
import com.maukaim.org.utils.serializer.converters.UserJsonConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(MkProcessAuditListener.class)
@Table(name = "t_process")
public class MkProcess implements Diffable<MkProcess> {
    @Id
    private UUID id;

    @Convert(converter = UserJsonConverter.class)
    private User manager;

    @Convert(converter = StepListJsonConverter.class)
    private List<Step> steps;

    private Double result;
    private LocalDateTime creationTime;

    @Override
    public DiffResult<MkProcess> diff(MkProcess process) {
        return DiffUtils.getGenericDiff(MkProcess.class, this, process);
    }

}
