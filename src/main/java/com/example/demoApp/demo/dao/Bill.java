package com.example.demoApp.demo.dao;

import javax.persistence.Entity;
import java.util.Date;


public class Bill {

    private String fromMobileNumber;
    private String toMobileNumber;
    private Date transactionDate;
    private int PrincipleAmount;
    private boolean isSettled;
    private boolean isPartial;
    private Date nextDate;
    private String picUrl;

}
