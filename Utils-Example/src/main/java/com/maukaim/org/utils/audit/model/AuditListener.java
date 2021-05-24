package com.maukaim.org.utils.audit.model;

import com.maukaim.org.utils.SpringCtx;

import javax.persistence.EntityManager;
import javax.persistence.MappedSuperclass;
import javax.transaction.Transactional;

import static javax.transaction.Transactional.TxType.MANDATORY;

@MappedSuperclass
public abstract class AuditListener {
    private Object previousTarget;

    abstract void prePersist(Object target);
    abstract void preUpdate(Object target);
    abstract void preRemove(Object target);
    abstract  Object createAuditObject(String action, Object target);

    @Transactional(MANDATORY)
    void perform(String action, Object target){
        if(!target.equals(previousTarget) || "DELETED".equalsIgnoreCase(action)){
            this.previousTarget = target;
            EntityManager entityManager = SpringCtx.getContext().getBean(EntityManager.class);
            Object objAudit = createAuditObject(action, target);
            entityManager.persist(objAudit);

        }
    }

}
