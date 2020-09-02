package com.example.demoApp.demo.Controller;

import com.example.demoApp.demo.Repositories.BillRepositories;
import com.example.demoApp.demo.dao.Bill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class BillController {

    @Autowired
    private BillRepositories billRepositories;

    @PostMapping(path = "/addBill", consumes = "application/json")
    public ResponseEntity addBill(@RequestBody Bill bill) {
        //to do : add validation
        bill.setTransactionDate(new Date());
        billRepositories.save(bill);
        return ResponseEntity.ok("Saved");
    }

    @GetMapping(path = "/getfromBill/{frommobileNumber}")
    public List<Bill> getfromBill(@RequestParam String frommobileNumber) {
        List<Bill> bills = billRepositories.findByFromMobileNumber(frommobileNumber);
        return bills;
    }

    @GetMapping(path = "/gettoBill/{tomobileNumber}")
    public List<Bill> getToBill(@RequestParam String toMobileNumber) {
        List<Bill> bills = billRepositories.findByToMobileNumber(toMobileNumber);
        return bills;
    }

//    @PostMapping(value = "/update", consumes = "application/json")
//    public ResponseEntity update(@RequestBody Bill bill) {
//        Optional<Bill> bills = billRepositories.findById(bill.getTr());
//        billRepositories.save(bills.get());
//        return ResponseEntity.ok("Updated");
//    }
}
