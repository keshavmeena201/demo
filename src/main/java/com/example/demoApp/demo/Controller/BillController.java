package com.example.demoApp.demo.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BillController {

    @PostMapping(path = "/addBill", consumes = "application/json")
    public ResponseEntity addBill() {
        return ResponseEntity.ok("");
    }
}
