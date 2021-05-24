package com.maukaim.org.utils.audit.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MkProcessAuditRepository extends JpaRepository<MkProcessAudit, String> {

    List<MkProcessAudit> getAllByIdEntity(String idEntity);

}
