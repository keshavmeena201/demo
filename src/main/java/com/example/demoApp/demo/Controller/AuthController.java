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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@RestController
public class AuthController {
    // auth/otp

    public static String otpUrl = "https://2factor.in/API/V1/%s/SMS/+91%s/AUTOGEN";
    public static String validateotpUrl = "https://2factor.in/API/V1/%s/SMS/VERIFY/%s/%s";
    public static String API_KEY = "c8ca0227-f1ac-11ea-9fa5-0200cd936042";

    @Autowired
    private TokenRepository tokenRepository;

    @PostMapping(path = "/sendOtp", consumes = "application/json")
    public ResponseEntity sendOtp(@RequestBody TokenRequestDao tokenRequestDao) {
        String url = String.format(otpUrl, API_KEY, tokenRequestDao.getPhoneNumber());
        SSLCertificateValidation.disable();
        ResponseEntity<String> response = null;
        //do https call
        JsonObject jsonObject = null;
        RestTemplate restTemplate = new RestTemplate();
        response = restTemplate.getForEntity(url, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            String  body = response.getBody();
            Gson g = new Gson();
            jsonObject = g.fromJson(body, JsonObject.class);
            System.out.println(jsonObject.get("Details"));
        }
        Token token = new Token();
        token.setToken(jsonObject.get("Details").getAsString());
        token.setMobileNo(tokenRequestDao.getPhoneNumber().toString());
        tokenRepository.save(token);
        com.example.demoApp.demo.dao.Response response1 = com.example.demoApp.demo.dao.Response.builder().message("OTP SENT").build();
        return ResponseEntity.ok(response1);
    }

    @GetMapping(value = "/validateotp/{otp}/{mobileNum}")
    public ResponseEntity validateOtp(@PathVariable String otp, @PathVariable String mobileNum) throws Exception{
        Optional<Token> token = tokenRepository.findById(mobileNum);
        System.out.println(token.toString());
        if(!token.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        String url = String.format(validateotpUrl, API_KEY, token.get().getToken(), otp);
        ResponseEntity<String> response = null;
        RestTemplate restTemplate = new RestTemplate();
        JsonObject jsonObject = null;
        response = restTemplate.getForEntity(url, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            String  body = response.getBody();
            Gson g = new Gson();
            jsonObject = g.fromJson(body, JsonObject.class);
            System.out.println(jsonObject.get("Details"));
        } else {
            return ResponseEntity.status(404).body("wrong otp");
        }
        com.example.demoApp.demo.dao.Response response1 = com.example.demoApp.demo.dao.Response.builder().message("validated").build();
        return ResponseEntity.ok(response1);
    }

    @GetMapping(value = "/store")
    public void get() {
        Token token = new Token();
        token.setMobileNo("9106729587");
        token.setToken("d03c1bb6-7b73-4bae-aedf-0d371eec02cc");
        tokenRepository.save(token);
        //http://localhost:8080/h2-console/
    }

    public static void main(String[] args) {
        System.out.println(String.format(AuthController.otpUrl,"x1","x2"));
//        TokenRequestDao tokenRequestDao = TokenRequestDao.builder().phoneNumber("9106729587").build();
//        new AuthController().sendOtp(tokenRequestDao);


    }


}
