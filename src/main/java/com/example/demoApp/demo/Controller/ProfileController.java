package com.example.demoApp.demo.Controller;

import com.example.demoApp.demo.Repositories.ProfileRepository;
import com.example.demoApp.demo.dao.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ProfileController {

    @Autowired
    private ProfileRepository profileRepository;

    @PostMapping(value = "/profileUpdate", consumes = "application/json")
    public ResponseEntity profileUpdate(@RequestBody Profile profile) {
        profileRepository.save(profile);
        return ResponseEntity.ok("added a new profile");
    }

    @GetMapping(value = "/profileUpdate/{mobileNum}", consumes = "application/json")
    public Profile profileUpdate(@PathVariable String mobileNum) {
        return profileRepository.findById(mobileNum).get();
    }

    @GetMapping(value = "/getScore/{mobileNumber}")
    public double getScore(@PathVariable String mobileNumber) {
        Optional<Profile> profileSelected = profileRepository.findById(mobileNumber);
        return profileSelected.get().getCreditScore();
    }
    @GetMapping(value = "/getAllProfiles")
    public ResponseEntity getAllProfiles() {
        List<Profile> allProfiles = (List<Profile>) profileRepository.findAll();
        return ResponseEntity.ok(allProfiles);
    }
}
