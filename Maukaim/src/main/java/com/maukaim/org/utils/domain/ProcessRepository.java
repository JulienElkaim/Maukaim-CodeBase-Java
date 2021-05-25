package com.maukaim.org.utils.domain;

import com.maukaim.org.utils.domain.model.MkProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProcessRepository extends JpaRepository<MkProcess, UUID> {

}
