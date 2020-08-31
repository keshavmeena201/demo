package com.example.demoApp.demo.Controller;

import com.example.demoApp.demo.Repositories.BillRepositories;
import com.example.demoApp.demo.dao.Bill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class BillController {

    @Autowired
    private BillRepositories billRepositories;

    @PostMapping(path = "/addBill", consumes = "application/json")
    public ResponseEntity addBill(@RequestBody Bill bill) {
        //to do : add validation
        billRepositories.save(bill);
        return ResponseEntity.ok("");
    }

    @GetMapping(path = "/getfromBill/{frommobileNumber}")
    public List<Bill> getBill(@RequestParam String frommobileNumber) {
        List<Bill> bills = billRepositories.findByFromMobileNumber(frommobileNumber);
        return bills;
    }

    @GetMapping(path = "/gettoBill/{tomobileNumber}")
    public List<Bill> getToBill(@RequestParam String toMobileNumber) {
        List<Bill> bills = billRepositories.findByToMobileNumber(toMobileNumber);
        return bills;
    }
}
