package com.example.demoApp.demo.Controller;

import com.example.demoApp.demo.dao.Bill;
import com.example.demoApp.demo.dao.Token;
import com.example.demoApp.demo.dao.TokenRequestDao;
import com.example.demoApp.demo.Repositories.BillRepositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import java.lang.Math;
import java.util.concurrent.TimeUnit;


@RestController
public class BillUpdate {

    @Autowired
    private BillRepositories billRepo;

    public static long getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    Integer getAmount (Integer amount) {
        if(amount <=30000) {
            return 0;
        }
        if (amount <= 50000) {
            return 30000;
        }
        if (amount <= 100000) {
            return 50000;
        }
        if (amount <= 500000) {
            return 100000;
        }
        return 500000;
    }

    double getCreditChange (Integer creditChange, Integer currentCreditScore, Integer transactionNumber) {
        if (currentCreditScore < 700) {
            return creditChange;
        }
        if (currentCreditScore < 750) {
            return creditChange/3.0;
        }
        if (currentCreditScore < 800) {
            return creditChange/9.0;
        }
        if (currentCreditScore < 850) {
            return Math.pow(0.33,transactionNumber) * creditChange/27;
        }
        return 0;
    }

    void paidDueDate (Bill bill) {
        Map<Integer,Integer> scoreTable = new HashMap<Integer, Integer>();
        scoreTable.put(0,5);
        scoreTable.put(30000,10);
        scoreTable.put(50000,20);
        scoreTable.put(100000,30);
        scoreTable.put(500000,50);

        Integer currentCreditScore = 600; // need to have the column in individual profile table

        Integer normalizedAmount = getAmount(bill.getAmountPaid());
        Integer creditChange = scoreTable.get(normalizedAmount);
        Integer normalizedChange = (int)Math.floor(getCreditChange(creditChange, currentCreditScore, bill.getTransactionNumber()));

        // update db by creditScore + normalizedChange
    }

    double getCreditChange2 (Integer creditChange, Integer normalizedAmount, Long diffDate) {
        if (diffDate < 10) {
            if (normalizedAmount == 0) {
                return creditChange + creditChange*0.2;
            }
            if (normalizedAmount == 30000) {
                return creditChange + creditChange*0.2;
            }
            if (normalizedAmount == 50000) {
                return creditChange + creditChange*0.3;
            }
            if (normalizedAmount == 100000) {
                return creditChange + creditChange*0.4;
            }
            if (normalizedAmount == 500000) {
                return creditChange + creditChange*0.5;
            }
        }
        if (diffDate < 20) {
            if (normalizedAmount == 0) {
                return creditChange + creditChange*0.1;
            }
            if (normalizedAmount == 30000) {
                return creditChange + creditChange*0.2;
            }
            if (normalizedAmount == 50000) {
                return creditChange + creditChange*0.3;
            }
            if (normalizedAmount == 100000) {
                return creditChange + creditChange*0.3;
            }
            if (normalizedAmount == 500000) {
                return creditChange + creditChange*0.4;
            }
        }
        if (diffDate < 30) {
            if (normalizedAmount == 0) {
                return creditChange + creditChange*0.05;
            }
            if (normalizedAmount == 30000) {
                return creditChange + creditChange*0.1;
            }
            if (normalizedAmount == 50000) {
                return creditChange + creditChange*0.2;
            }
            if (normalizedAmount == 100000) {
                return creditChange + creditChange*0.2;
            }
            if (normalizedAmount == 500000) {
                return creditChange + creditChange*0.3;
            }
        }
        return 0;
    }

    void paidPostDueDate(Bill bill) {
        Map<Integer,Integer> scoreTable = new HashMap<Integer, Integer>();;
        scoreTable.put(0,-50);
        scoreTable.put(30000,-30);
        scoreTable.put(50000,-25);
        scoreTable.put(100000,-35);
        scoreTable.put(500000,-50);

        Integer currentCreditScore = 600; // get from db with a separate table
        Long diffDate = getDifferenceDays(bill.getTransactionDate(), bill.getTransactionDueDate());

        Integer normalizedAmount = getAmount(bill.getAmountPaid());
        Integer creditChange = scoreTable.get(normalizedAmount);
        Integer normalizedChange = (int)Math.floor(getCreditChange2(creditChange, normalizedAmount, diffDate));
        // update db by creditScore + normalizedChange
    }

    double getCreditChange3 (Integer creditChange, int transactionNumber, double oldRatio, double newRatio) {
        if(transactionNumber == 1) {
            return creditChange * (oldRatio) * (1-oldRatio);
        }
        else {
            return creditChange * (oldRatio) * (1-oldRatio) * (newRatio);
        }
    }
    void paidPartial(Bill bill) {
        Map<Integer,Integer> scoreTable = new HashMap<Integer, Integer>();;
        scoreTable.put(0,-50);
        scoreTable.put(30000,-30);
        scoreTable.put(50000,-25);
        scoreTable.put(100000,-35);
        scoreTable.put(500000,-50);

        Integer currentCreditScore = 600;
        double newRatio = 1.0*bill.getAmountPaid()/bill.getPrincipleAmount();
        if(bill.getTransactionNumber() == 0) {
            bill.setOldRatio(newRatio);
        }
        Integer normalizedAmount = getAmount(bill.getAmountPaid());
        Integer creditChange = scoreTable.get(normalizedAmount);
        Integer normalizedChange = (int)Math.floor(getCreditChange3(creditChange, bill.getTransactionNumber(), bill.getOldRatio(), newRatio));
        // update db by creditScore + normalizedChange
    }

    void callUpdate(Bill bill) {
        if (bill.isSettled() && bill.getTransactionDate() == bill.getTransactionDueDate()) {
            paidDueDate(bill);
        }
        if (bill.isSettled() && bill.getTransactionDate() != bill.getTransactionDueDate()) {
            paidPostDueDate(bill);
        }
        if (bill.isPartial()) {
            paidPartial(bill);
        }
    }
    @PostMapping (path = "/updateBill", consumes = "application/json")
    public ResponseEntity updateBillData (@RequestBody Bill billData) {
        billRepo.save(billData);
        callUpdate(billData);
        return ResponseEntity.ok("updated Bill");
    }
    @GetMapping (value="/getAllBills")
    public ResponseEntity getAllBills() {
        List<Bill> allBill = (List<Bill>) billRepo.findAll();
        return ResponseEntity.ok(allBill);
    }
    @GetMapping (value="/getBillData/{transactionId}")
    public ResponseEntity getBill(@PathVariable String transactionId) {
        Optional<Bill> singleBill = billRepo.findById(transactionId);
        return  ResponseEntity.ok(singleBill);
    }
    @PostMapping (path = "/refreshScore/{transactionId}")
    public ResponseEntity RefreshScore (@PathVariable String transactionId) {
        Optional<Bill> singleBill = billRepo.findById(transactionId);
        callUpdate(singleBill.get());
        return ResponseEntity.ok(singleBill);
    }

    // post request for adding next payment linked to previous one.
    @PostMapping (path = "/addNextPayment/{previousTransactionId}", consumes = "application/json")
    public ResponseEntity addNextPayment(@RequestBody Bill billData, @PathVariable String previousTransactionId) {
        billData.setPreviousTransactionId(previousTransactionId);
        Optional<Bill> previousBill = billRepo.findById(previousTransactionId);
        billData.setTransactionNumber(previousBill.get().getTransactionNumber()+1);
        billRepo.save(billData);
        callUpdate(billData);
        return ResponseEntity.ok("Payment Added");
    }
}
