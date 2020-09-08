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
        Optional<Profile> profile1 = profileRepository.findById(profile.getMobileNumber());
        if(profile1.isPresent()){
            profile1.get().setOrgName(profile.getOrgName());
            profileRepository.save(profile1.get());
        }else {
            profile.setCreditScore(600);
            profile.setAmountToGive(0);
            profile.setAmountToPay(0);
            profileRepository.save(profile);
        }

        return ResponseEntity.ok("{added a new profile}");
    }

    @GetMapping(value = "/getProfile/{mobileNum}")
    public ResponseEntity profileUpdate(@PathVariable String mobileNum) {
        return ResponseEntity.ok(profileRepository.findById(mobileNum).get());
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
