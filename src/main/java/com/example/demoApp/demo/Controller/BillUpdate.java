package com.example.demoApp.demo.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import java.lang.Math;

@RestController
public class BillUpdate {
    // get bill data .
    private String fromMobileNumber;
    private String toMobileNumber;
    private Date transactionDate;
    private int PrincipleAmount;
    private boolean isSettled;
    private boolean isPartial;
    private Date nextDate;

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

    double getCreditChange (Integer creditChange, Integer currentCreditScore, Integer txnNo) {
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
            return Math.pow(0.33,txnNo) * creditChange/27;
        }
        return 0;
    }

    void paidDueDate () {
        Map<Integer,Integer> scoreTable = new HashMap<Integer, Integer>();;
        scoreTable.put(0,5);
        scoreTable.put(30000,10);
        scoreTable.put(50000,20);
        scoreTable.put(100000,30);
        scoreTable.put(500000,50);

        Integer currentCreditScore = 600;
        Integer txnNo = 2;

        Integer normalizedAmount = getAmount(PrincipleAmount);
        Integer creditChange = scoreTable.get(normalizedAmount);
        Integer normalizedChange = (int)Math.floor(getCreditChange(creditChange, currentCreditScore, txnNo));
        // update the db.
    }

    double getCreditChange2 (Integer creditChange, Integer normalizedAmount, Integer diffDate) {
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

    void paidPostDueDate () {
        Map<Integer,Integer> scoreTable = new HashMap<Integer, Integer>();;
        scoreTable.put(0,-50);
        scoreTable.put(30000,-30);
        scoreTable.put(50000,-25);
        scoreTable.put(100000,-35);
        scoreTable.put(500000,-50);

        Integer currentCreditScore = 600;
        Integer diffDate = 2;

        Integer normalizedAmount = getAmount(PrincipleAmount);
        Integer creditChange = scoreTable.get(normalizedAmount);
        Integer normalizedChange = (int)Math.floor(getCreditChange2(creditChange, normalizedAmount, diffDate));
        // update db
    }

    double getCreditChange3 (Integer creditChange, int txnNo, double oldRatio, double newRatio) {
        if(txnNo == 1) {
            return creditChange * (oldRatio) * (1-oldRatio);
        }
        else {
            return creditChange * (oldRatio) * (1-oldRatio) * (newRatio);
        }
    }
    void paidPartial () {
        Map<Integer,Integer> scoreTable = new HashMap<Integer, Integer>();;
        scoreTable.put(0,-50);
        scoreTable.put(30000,-30);
        scoreTable.put(50000,-25);
        scoreTable.put(100000,-35);
        scoreTable.put(500000,-50);

        Integer currentCreditScore = 600;
        Integer txnNo = 5;
        double oldRatio = 0.2;
        double newRatio = 0.5;

        Integer normalizedAmount = getAmount(PrincipleAmount);
        Integer creditChange = scoreTable.get(normalizedAmount);
        Integer normalizedChange = (int)Math.floor(getCreditChange3(creditChange, txnNo, oldRatio, newRatio));
        // update db
    }

    void callUpdate(boolean isSettled, boolean isPartial) {
        if (isSettled) {
            paidDueDate();
        }
        if (!isSettled) {
            paidPostDueDate();
        }
        if (isPartial) {
            paidPartial();
        }
    }
}
