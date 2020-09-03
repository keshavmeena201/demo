package com.example.demoApp.demo.dao;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Profile {
    @Id
    private String mobileNumber;
    private String orgName;
    private int creditScore;
    private int amountToGive;
    private int amountToPay;
}
