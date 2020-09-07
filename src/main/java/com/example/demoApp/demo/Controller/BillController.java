package com.example.demoApp.demo.Controller;

import com.example.demoApp.demo.Repositories.BillRepositories;
import com.example.demoApp.demo.dao.Bill;
import com.google.gson.Gson;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    @GetMapping(path = "/getallTransactions/{frommobileNumber}")
    public ResponseEntity<List<Bill>> getTransactions(@PathVariable String frommobileNumber) {
        List<Bill> bills = billRepositories.findByFromMobileNumber(frommobileNumber);
        List<Bill> bills1 = billRepositories.findByToMobileNumber(frommobileNumber);
        bills.addAll(bills1);
        System.out.println(bills);
        Gson gson = new Gson();
        System.out.print(ResponseEntity.ok(bills).getBody().toString());
        return ResponseEntity.ok(bills);
    }

//    @PostMapping(value = "/update", consumes = "application/json")
//    public ResponseEntity update(@RequestBody Bill bill) {
//        Optional<Bill> bills = billRepositories.findById(bill.getTr());
//        billRepositories.save(bills.get());
//        return ResponseEntity.ok("Updated");
//    }
}
