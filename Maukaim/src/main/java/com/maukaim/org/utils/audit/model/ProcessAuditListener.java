package com.maukaim.org.utils.audit.model;

import com.maukaim.org.utils.model.Process;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class ProcessAuditListener extends AuditListener {
    @PrePersist
    void prePersist(Object target) {
        perform("CREATED", target);
    }

    @PreUpdate
    void preUpdate(Object target) {
        perform("UPDATED", target);

    }

    @PreRemove
    void preRemove(Object target) {
        perform("DELETED", target);

    }

    @Override
    Object createAuditObject(String action, Object target) {
        return new ProcessAudit(action, (Process)target);
    }
}
