package com.example.demoApp.demo.Controller;

import com.example.demoApp.demo.Repositories.TokenRepository;
import com.example.demoApp.demo.Util.SSLCertificateValidation;
import com.example.demoApp.demo.dao.Token;
import com.example.demoApp.demo.dao.TokenRequestDao;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Controller
public class AuthController {
    // auth/otp

    public static String otpUrl = "https://2factor.in/API/V1/%s/SMS/+91%s/AUTOGEN";
    public static String validateotpUrl = "https://2factor.in/API/V1/%s/SMS/VERIFY/%s/%s";
    public static String API_KEY = "849f54db-e53b-11ea-9fa5-0200cd936042";

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private  RestTemplate restTemplate;

    @PostMapping(path = "/sendOtp", consumes = "application/json", produces = "application/json")
    public void sendOtp( @RequestBody TokenRequestDao tokenRequestDao) {
        String url = String.format(otpUrl, API_KEY, tokenRequestDao.getPhoneNumber());
        SSLCertificateValidation.disable();
        ResponseEntity<String> response = null;
        //do https call
        JsonObject jsonObject = null;
        restTemplate = new RestTemplate();
        response = restTemplate.getForEntity(url, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            String  body = response.getBody();
            Gson g = new Gson();
            jsonObject = g.fromJson(body, JsonObject.class);
            System.out.println(jsonObject.get("Details"));
        }
        Token token = Token.builder().token(jsonObject.get("Details").toString()).mobileNo(tokenRequestDao.getPhoneNumber()).build();
        tokenRepository.save(token);
    }

    @GetMapping(value = "/validateotp/{otp}/{mobileNum}")
    public void validateOtp(@RequestParam String otp, @RequestParam String mobileNum) throws Exception{
        Optional<Token> token = tokenRepository.findById(mobileNum);
        if(!token.isPresent()) {
            return ;
        }
        String url = String.format(validateotpUrl, API_KEY, token.get().getToken(), otp);
        ResponseEntity<String> response = null;
        restTemplate = new RestTemplate();
        JsonObject jsonObject = null;
        response = restTemplate.getForEntity(url, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            String  body = response.getBody();
            Gson g = new Gson();
            jsonObject = g.fromJson(body, JsonObject.class);
            System.out.println(jsonObject.get("Details"));
            //check if details element has "OTP Matched"
        } else {
            //throw exception
        }
        //do http call
    }

    public static void main(String[] args) {
        System.out.println(String.format(AuthController.otpUrl,"x1","x2"));
        TokenRequestDao tokenRequestDao = TokenRequestDao.builder().phoneNumber("9106729587").build();
        new AuthController().sendOtp(tokenRequestDao);


    }


}
