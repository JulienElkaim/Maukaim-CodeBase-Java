package com.maukaim.org.utils.web;

import com.maukaim.org.utils.audit.demo.MkProcessAuditService;
import com.maukaim.org.utils.audit.model.MkProcessAudit;
import com.maukaim.org.utils.diffable.demo.MkProcessDifferencesService;
import com.maukaim.org.utils.diffable.model.ProcessUpdate;
import com.maukaim.org.utils.domain.model.mock.MkProcessMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("api/v1/diffable-example")
public class DiffableExampleController {
    @Autowired
    private MkProcessDifferencesService processDifferencesService;

    @GetMapping(value = "/first")
    public ResponseEntity<List<ProcessUpdate>> getFirstExample() {
        return ResponseEntity.ok(this.processDifferencesService.getDiffs(MkProcessMock.FIRST_ID.toString()));

    }

    @GetMapping(value = "/second")
    public ResponseEntity<List<ProcessUpdate>> getSecondExample() {
        return ResponseEntity.ok(this.processDifferencesService.getDiffs(MkProcessMock.SECOND_ID.toString()));

    }

    @GetMapping(value = "/third")
    public ResponseEntity<List<ProcessUpdate>> getThirdExample() {
        return ResponseEntity.ok(this.processDifferencesService.getDiffs(MkProcessMock.THIRD_ID.toString()));
    }

}
