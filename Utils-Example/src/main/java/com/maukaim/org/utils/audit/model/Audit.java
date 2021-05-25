package com.maukaim.org.utils.audit.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@NoArgsConstructor
@MappedSuperclass
@SuperBuilder
@Getter
abstract class Audit {
    @Id
    @GeneratedValue
    private Integer idRevision;
    private String idEntity;
    private String action;
    private String modifiedBy;
    private LocalDateTime modifiedDate;

    Audit(String action, String idEntity){
        this.action = action;
        this.idEntity = idEntity;
        this.modifiedBy = "RANDOM USER NAME";
        this.modifiedDate = LocalDateTime.now();
    }
}
