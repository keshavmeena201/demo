package com.example.demoApp.demo.dao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fromMobileNumber;
    private String toMobileNumber;
    private Date transactionDate;
    private int PrincipleAmount;
    private boolean isSettled;
    private boolean isPartial;
    private Date nextDate;
    private String picUrl;
    private int amountpaid;
    private float oldratio;
    private int transactionNumber;

}
