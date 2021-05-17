package com.maukaim.org.utils.audit.model;

import com.maukaim.org.utils.model.Process;
import com.maukaim.org.utils.serializer.converters.ProcessJsonConverter;
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
public class ProcessAudit extends Audit {
    @Column(length = 600000)
    @Convert(converter = ProcessJsonConverter.class)
    private Process process;

    public ProcessAudit(String action, Process process){
        super(action, process.getId().toString());
        this.process = process;
    }
}
