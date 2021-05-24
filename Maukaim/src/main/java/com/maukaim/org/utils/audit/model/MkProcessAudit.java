package com.maukaim.org.utils.audit.model;

import com.maukaim.org.utils.domain.model.MkProcess;
import com.maukaim.org.utils.serializer.converters.MkProcessJsonConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name="t_process_audit")
@NoArgsConstructor
public class MkProcessAudit extends Audit {
    @Column(length = 600000)
    @Convert(converter = MkProcessJsonConverter.class)
    private MkProcess process;

    public MkProcessAudit(String action, MkProcess process){
        super(action, process.getId().toString());
        this.process = process;
    }
}
