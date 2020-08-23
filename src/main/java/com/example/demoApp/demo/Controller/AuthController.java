package com.example.demoApp.demo.Controller;

import com.example.demoApp.demo.Repositories.TokenRepository;
import com.example.demoApp.demo.Util.SSLCertificateValidation;
import com.example.demoApp.demo.dao.Token;
import com.example.demoApp.demo.dao.TokenRequestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Controller
public class AuthController {
    // auth/otp

    private static String otpUrl = "https://2factor.in/API/V1/%s/SMS/+91%s/AUTOGEN";
    private static String validateotpUrl = "https://2factor.in/API/V1/%s/SMS/VERIFY/%s/%s";
    public static String API_KEY = "key";

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private  RestTemplate restTemplate;

    @PostMapping(path = "/sendOtp", consumes = "application/json", produces = "application/json")
    public void sendOtp( @RequestBody TokenRequestDao tokenRequestDao) {
        String url = String.format(otpUrl, API_KEY, tokenRequestDao.getPhoneNumber());
        SSLCertificateValidation.disable();
        String response = null;
        //do https call
        // response = restTemplate.
        Token token = Token.builder().token(response).mobileNo(tokenRequestDao.getPhoneNumber()).build();
        tokenRepository.save(token);
    }

    @GetMapping(value = "/validateotp/{otp}/{mobileNum}")
    public void validateOtp(@RequestParam String otp, @RequestParam String mobileNum) throws Exception{
        Optional<Token> token = tokenRepository.findById(mobileNum);
        if(!token.isPresent()) {
            return;
        }
        String url = String.format(validateotpUrl, API_KEY, token.get().getToken(), otp);
        //do http call


    }


}
