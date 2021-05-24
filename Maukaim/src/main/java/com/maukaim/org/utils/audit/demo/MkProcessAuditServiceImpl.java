package com.maukaim.org.utils.audit.demo;

import com.maukaim.org.utils.audit.model.MkProcessAudit;
import com.maukaim.org.utils.audit.model.MkProcessAuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MkProcessAuditServiceImpl implements MkProcessAuditService {

    private final MkProcessAuditRepository auditRepository;

    MkProcessAuditServiceImpl(@Autowired MkProcessAuditRepository repo){
        this.auditRepository = repo;
    }
    @Override
    public List<MkProcessAudit> getAll() {
        return this.auditRepository.findAll();
    }
}
