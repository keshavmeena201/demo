package com.example.demoApp.demo.dao;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class Bill {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String transactionId;
    private String fromMobileNumber;
    private String toMobileNumber;
    private Date transactionDueDate;
    private Date transactionDate;
    private int principleAmount;
    private boolean isSettled;
    private boolean isPartial;
    private String nextDate;
    private String picUrl;
    private int amountPaid;
    private double oldRatio;
    private boolean scoreUpdated;
    private int transactionNumber;
    private String previousTransactionId;
    private String fromName;
    private String toName;
    private boolean transactionType;

}
