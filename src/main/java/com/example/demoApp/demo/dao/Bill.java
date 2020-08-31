package com.example.demoApp.demo.dao;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@IdClass(CompositeKey.class)
public class Bill {
    @Id
    private Long id;
    @Id
    private String fromMobileNumber;
    @Id
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
