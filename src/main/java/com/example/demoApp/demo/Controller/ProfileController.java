package com.example.demoApp.demo.Controller;

import com.example.demoApp.demo.Repositories.ProfileRepository;
import com.example.demoApp.demo.dao.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProfileController {

    @Autowired
    private ProfileRepository profileRepository;

    @PostMapping(value = "/profileUpdate", consumes = "application/json")
    public ResponseEntity profileUpdate(@RequestBody Profile profile) {
        profileRepository.save(profile);
        return ResponseEntity.ok("added");
    }

    @GetMapping(value = "/profileUpdate/{mobileNum}", consumes = "application/json")
    public Profile profileUpdate(@PathVariable String mobileNum) {
        return profileRepository.findById(mobileNum).get();
    }

}
