package com.maukaim.org.utils.diffable.demo;


import com.maukaim.org.utils.audit.model.MkProcessAudit;
import com.maukaim.org.utils.audit.model.MkProcessAuditRepository;
import com.maukaim.org.utils.diffable.model.ProcessUpdate;
import com.maukaim.org.utils.domain.model.MkProcess;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class MkProcessDifferencesServiceTests {

    @Mock
    private MkProcessAuditRepository auditRepository;

    @Test
    public void getDiffs_should_return_n_minus_one_items_on_n_audit_items(){
        List<MkProcessAudit> mkProcessAudits = List.of(
                MkProcessAudit.builder().process(MkProcess.builder().result(140D).build()).action("UPDATE").build(),
                MkProcessAudit.builder().process(MkProcess.builder().result(10D).build()).action("UPDATE").build(),
                MkProcessAudit.builder().process(MkProcess.builder().result(150D).build()).action("UPDATE").build(),
                MkProcessAudit.builder().process(MkProcess.builder().result(140D).build()).action("UPDATE").build()
        );
        Mockito.when(this.auditRepository.getAllByIdEntity(any())).thenReturn(mkProcessAudits);

        MkProcessDifferencesServiceImpl service = new MkProcessDifferencesServiceImpl(auditRepository);

        List<ProcessUpdate> updates = service.getDiffs("uselessId");

        Mockito.verify(auditRepository, Mockito.times(1)).getAllByIdEntity(any());
        Assert.assertEquals("Should get n-1 updates", this.auditRepository.getAllByIdEntity("useLessId").size()-1,updates.size());
    }
}
