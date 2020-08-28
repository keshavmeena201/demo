package com.example.demoApp.demo.dao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Token {

    @Id
    private String mobileNo;

    @Column(name = "token")
    private String token;
}
