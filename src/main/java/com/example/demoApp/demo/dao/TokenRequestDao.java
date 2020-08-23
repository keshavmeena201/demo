package com.example.demoApp.demo.dao;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder
public class TokenRequestDao {

    private String phoneNumber;
}
