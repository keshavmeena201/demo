package com.example.demoApp.demo.Controller;

import com.example.demoApp.demo.Repositories.ProfileRepository;
import com.example.demoApp.demo.dao.Bill;
import com.example.demoApp.demo.dao.Profile;
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
    @Autowired
    private ProfileRepository profileRepo;

    public double updateScore (String mobileNumber, double scoreChange) {
//        System.out.print(mobileNumber);
        Optional<Profile> currentProfile = profileRepo.findById(mobileNumber);
        double currentScore = currentProfile.get().getCreditScore();
        currentProfile.get().setCreditScore(currentScore + scoreChange);
        profileRepo.save(currentProfile.get());
        return currentScore + scoreChange;
    }

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

    double paidDueDate (Bill bill) {
        Map<Integer,Integer> scoreTable = new HashMap<Integer, Integer>();
        scoreTable.put(0,5);
        scoreTable.put(30000,10);
        scoreTable.put(50000,20);
        scoreTable.put(100000,30);
        scoreTable.put(500000,50);

        double currentCreditScore = profileRepo.findById(bill.getFromMobileNumber()).get().getCreditScore(); // need to have the column in individual profile table

        Integer normalizedAmount = getAmount(bill.getAmountPaid());
        Integer creditChange = scoreTable.get(normalizedAmount);
        double normalizedChange = (getCreditChange(creditChange, (int) currentCreditScore, bill.getTransactionNumber()));

        return updateScore(bill.getFromMobileNumber(), normalizedChange);
        // update db by creditScore + normalizedChange
    }

    double getCreditChange2 (Integer creditChange, Integer normalizedAmount, Long diffDate) {
        if (diffDate < 10) {
            if (normalizedAmount == 0) {
                return creditChange - creditChange*0.2;
            }
            if (normalizedAmount == 30000) {
                return creditChange - creditChange*0.2;
            }
            if (normalizedAmount == 50000) {
                return creditChange - creditChange*0.3;
            }
            if (normalizedAmount == 100000) {
                return creditChange - creditChange*0.4;
            }
            if (normalizedAmount == 500000) {
                return creditChange - creditChange*0.5;
            }
        }
        if (diffDate < 20) {
            if (normalizedAmount == 0) {
                return creditChange - creditChange*0.1;
            }
            if (normalizedAmount == 30000) {
                return creditChange - creditChange*0.2;
            }
            if (normalizedAmount == 50000) {
                return creditChange - creditChange*0.3;
            }
            if (normalizedAmount == 100000) {
                return creditChange - creditChange*0.3;
            }
            if (normalizedAmount == 500000) {
                return creditChange - creditChange*0.4;
            }
        }
        if (diffDate < 30) {
            if (normalizedAmount == 0) {
                return creditChange - creditChange*0.05;
            }
            if (normalizedAmount == 30000) {
                return creditChange - creditChange*0.1;
            }
            if (normalizedAmount == 50000) {
                return creditChange - creditChange*0.2;
            }
            if (normalizedAmount == 100000) {
                return creditChange - creditChange*0.2;
            }
            if (normalizedAmount == 500000) {
                return creditChange - creditChange*0.3;
            }
        }
        return 0;
    }

    double paidPostDueDate(Bill bill) {
        Map<Integer,Integer> scoreTable = new HashMap<Integer, Integer>();;
        scoreTable.put(0,-50);
        scoreTable.put(30000,-30);
        scoreTable.put(50000,-25);
        scoreTable.put(100000,-35);
        scoreTable.put(500000,-50);

        double currentCreditScore = profileRepo.findById(bill.getFromMobileNumber()).get().getCreditScore(); // get from db with a separate table
        Long diffDate = getDifferenceDays(bill.getTransactionDate(), bill.getTransactionDueDate());

        Integer normalizedAmount = getAmount(bill.getAmountPaid());
        Integer creditChange = scoreTable.get(normalizedAmount);
        double normalizedChange = (getCreditChange2(creditChange, normalizedAmount, diffDate));

        return updateScore(bill.getFromMobileNumber(), normalizedChange);
    }

    double getCreditChange3 (Integer creditChange, int transactionNumber, double oldRatio, double newRatio, boolean isSettled) {
        if(transactionNumber == 0) {
            return creditChange*oldRatio;
        }
        else if (transactionNumber == 1 && isSettled) {
            return creditChange * (oldRatio) * (1-oldRatio) * (newRatio);
        }
        else {
            return creditChange*Math.pow(oldRatio,transactionNumber)*(1-oldRatio)*newRatio;
        }
    }
    double paidPartial(Bill bill) {
        Map<Integer,Integer> scoreTable = new HashMap<Integer, Integer>();;
        scoreTable.put(0,5);
        scoreTable.put(30000,10);
        scoreTable.put(50000,20);
        scoreTable.put(100000,30);
        scoreTable.put(500000,50);

        double currentCreditScore = profileRepo.findById(bill.getFromMobileNumber()).get().getCreditScore();
        double newRatio = 1.0*bill.getAmountPaid()/bill.getPrincipleAmount();
        if(bill.getTransactionNumber() == 0) {
            bill.setOldRatio(newRatio);
            billRepo.save(bill);
        }
        Integer normalizedAmount = getAmount(bill.getAmountPaid());
        Integer creditChange = scoreTable.get(normalizedAmount);
        double normalizedChange;
        normalizedChange = (getCreditChange3(creditChange, bill.getTransactionNumber(), bill.getOldRatio(), newRatio, bill.isSettled()));
        System.out.print(normalizedChange);
        return updateScore(bill.getFromMobileNumber(), normalizedChange);
        // update db by creditScore + normalizedChange
    }

    double callUpdate(Bill bill) {
//        System.out.print("yes");
        int checkDate = bill.getTransactionDueDate().compareTo(bill.getTransactionDate());
//        System.out.print(bill.isSettled());
        if (bill.isSettled() && checkDate == 0) {
            System.out.print("no");
            return paidDueDate(bill);
        }
        else if (bill.isSettled() && bill.getTransactionDate() != bill.getTransactionDueDate()) {
            System.out.print("yesno");
            return paidPostDueDate(bill);
        }
        else if (bill.isPartial()) {
            return paidPartial(bill);
        }
        return 0;
    }
    @PostMapping (path = "/updateBill", consumes = "application/json")
    public ResponseEntity updateBillData (@RequestBody Bill billData) {
//        System.out.print(billData);
        billRepo.save(billData);
        double finalScore = callUpdate(billData);
        return ResponseEntity.ok(finalScore);
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
        previousBill.get().setSettled(true); // because new bill will be created with unsettled option. So old bill should be discarded
        billData.setOldRatio(previousBill.get().getOldRatio());
        billData.setPrincipleAmount(previousBill.get().getPrincipleAmount()-billData.getAmountPaid());
        billRepo.save(billData);
        callUpdate(billData);
        return ResponseEntity.ok("Payment Added");
    }

//    @GetMapping (value = "getUnsettledBills/{mobileNumber}")
//    public List<Bill> getUnsettledBills(@PathVariable String mobileNumber) {
//        return billRepo.findByUnSettled(mobileNumber);
//    }
}
