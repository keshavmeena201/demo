package com.example.demoApp.demo.dao;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
//@IdClass(CompositeKey.class)
public class Bill {
    @Id
    private String transactionId;

    private String fromMobileNumber;
    private String toMobileNumber;
    private Date transactionDueDate;
    private Date transactionDate;
    private int principleAmount;
    private boolean isSettled;
    private boolean isPartial;
    private Date nextDate;
    private String picUrl;
    private int amountPaid;
    private double oldRatio;
    private boolean scoreUpdated;
    private int transactionNumber;
    private String previousTransactionId;

}
