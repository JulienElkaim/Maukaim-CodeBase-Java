package com.maukaim.org.utils.diffable.demo;

import com.maukaim.org.utils.audit.model.MkProcessAudit;
import com.maukaim.org.utils.audit.model.MkProcessAuditRepository;
import com.maukaim.org.utils.diffable.model.ProcessUpdate;
import com.maukaim.org.utils.diffable.model.ProcessUpdateFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MkProcessDifferencesServiceImpl implements MkProcessDifferencesService {


    private final MkProcessAuditRepository auditRepository;

    MkProcessDifferencesServiceImpl(@Autowired MkProcessAuditRepository repo) {
        this.auditRepository = repo;
    }

    @Override
    public List<ProcessUpdate> getDiffs(String idEntity) {
        List<MkProcessAudit> allByIdEntity = this.auditRepository.getAllByIdEntity(idEntity);
        return ProcessUpdateFactory.build(allByIdEntity);
    }
}
