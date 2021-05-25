package com.maukaim.org.utils.config;

import com.maukaim.org.utils.audit.model.MkProcessAudit;
import com.maukaim.org.utils.audit.model.MkProcessAuditRepository;
import com.maukaim.org.utils.domain.model.MkProcess;
import com.maukaim.org.utils.domain.ProcessRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EntityScan(
        basePackageClasses = {
                MkProcess.class,
                MkProcessAudit.class
        }
)
@EnableJpaRepositories(basePackageClasses = {
        ProcessRepository.class,
        MkProcessAuditRepository.class
})
@EnableTransactionManagement
@EnableJpaAuditing
public class JpaConfig {
}
