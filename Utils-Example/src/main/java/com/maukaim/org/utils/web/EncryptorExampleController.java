package com.maukaim.org.utils.web;

import com.maukaim.org.utils.audit.demo.MkProcessAuditService;
import com.maukaim.org.utils.audit.model.MkProcessAudit;
import com.maukaim.org.utils.confidential.EncryptorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("api/v1/encrypt-example")
public class EncryptorExampleController {
    @Autowired
    private EncryptorService encryptorService;

    @PostMapping(value = "/encrypt")
    public ResponseEntity<String> encrypt(@RequestParam String value) {
        try {
            return ResponseEntity.ok(this.encryptorService.encrypt(value));
        } catch (Exception e) {
            log.error("Exception while encrypting input {}", value);
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @PostMapping(value = "/decrypt")
    public ResponseEntity<String> decrypt(@RequestParam String value) {
        try {
            return ResponseEntity.ok(this.encryptorService.decrypt(value));
        } catch (Exception e) {
            log.error("Exception while decrypting input {}", value);
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @PostMapping(value = "/is-encrypted")
    public ResponseEntity<Boolean> isEncrypted(@RequestParam String value) {
            return ResponseEntity.ok(this.encryptorService.isEncrypted(value));
    }
}
