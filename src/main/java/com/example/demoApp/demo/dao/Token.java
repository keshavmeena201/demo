package com.example.demoApp.demo.dao;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Builder
@ToString
@Entity
public class Token {

    @Id
    private String mobileNo;

    @Column(name = "token")
    private String token;
}
