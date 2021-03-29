package com.maukaim.org.swaggerSBC.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("api/v1/basicExample")
public class BasicController {

    @GetMapping(value = "/testMapping")
    public ResponseEntity<String> getTest() {
        return ResponseEntity.ok("Good, I received the request.");
    }


    @GetMapping(value = "/testMappingParam")
    public ResponseEntity<String> getTestWithParam(@RequestParam String param) {
        return ResponseEntity.ok(String.format("I received the Request Param %s .", param));
    }

    @GetMapping(value = "/testMappingVariable/{variable}")
    public ResponseEntity<String> getTestWithVariable(@PathVariable String variable) {
        return ResponseEntity.ok(String.format("I received the Path variable %s .", variable));
    }

    @PostMapping(value = "/testMapping")
    public ResponseEntity<String> postTest(@RequestBody String bodyTest,
                                           @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
                                                   LocalDate date) {
        return ResponseEntity.ok(String.format("I received: Body %s and Request Param %s",
                bodyTest,
                date.format(DateTimeFormatter.ISO_DATE)));
    }

}
