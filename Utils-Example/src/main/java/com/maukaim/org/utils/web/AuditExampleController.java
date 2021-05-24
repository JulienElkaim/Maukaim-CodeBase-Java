package com.maukaim.org.utils.web;

import com.maukaim.org.utils.audit.demo.MkProcessAuditService;
import com.maukaim.org.utils.audit.model.MkProcessAudit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/audit-example")
public class AuditExampleController {
    @Autowired
    private MkProcessAuditService processAuditService;

    @GetMapping(value = "/testMapping")
    public ResponseEntity<List<MkProcessAudit>> getExample() {
        return ResponseEntity.ok(this.processAuditService.getAll());

    }

}
