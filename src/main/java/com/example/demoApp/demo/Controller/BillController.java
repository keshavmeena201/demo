package com.example.demoApp.demo.Controller;

import com.example.demoApp.demo.Repositories.BillRepositories;
import com.example.demoApp.demo.Repositories.ProfileRepository;
import com.example.demoApp.demo.dao.Bill;
import com.example.demoApp.demo.dao.Profile;
import com.google.gson.Gson;
import org.apache.catalina.connector.Response;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.*;

@RestController
public class BillController {

    @Autowired
    private BillRepositories billRepositories;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private BillUpdate billUpdate;

    @PostMapping(path = "/addBill", consumes = "application/json")
    public ResponseEntity addBill(@RequestBody Bill bill) {
        //to do : add validation
        bill.setTransactionDate(new Date());
        bill.setToMobileNumber(getMobileNumber(bill.getToMobileNumber()));
        bill.setFromMobileNumber(getMobileNumber(bill.getFromMobileNumber()));
        billRepositories.save(bill);
        updateProfile(bill);
        com.example.demoApp.demo.dao.Response response = com.example.demoApp.demo.dao.Response.builder().message("saved").build();

        return ResponseEntity.ok(response);
    }

    private void updateProfile(Bill bill) {
        Optional<Profile> profileFrom = profileRepository.findById(bill.getFromMobileNumber());
        Optional<Profile> profileTo = profileRepository.findById(bill.getToMobileNumber());
        if (!profileFrom.isPresent()) {
            profileFrom = setProfile(bill.getFromMobileNumber());
        }
        if (!profileTo.isPresent()) {
            profileTo = setProfile(bill.getToMobileNumber());
        }
        profileFrom.get().setAmountToPay(profileFrom.get().getAmountToPay()+bill.getPrincipleAmount());
        profileTo.get().setAmountToGive(profileTo.get().getAmountToGive()+bill.getPrincipleAmount());
        List<Profile> list = new ArrayList<>();
        list.add(profileFrom.get());
        list.add(profileTo.get());
        profileRepository.saveAll(list);
    }

    public Optional<Profile> setProfile(String mobileNumber) {
        Profile profile = new Profile();
        profile.setCreditScore(600);
        profile.setAmountToGive(0);
        profile.setAmountToPay(0);
        profile.setMobileNumber(mobileNumber);
        return Optional.of(profile);
    }
    public String  getMobileNumber(String mobileNumber) {
        return mobileNumber.replace("+91","").replace(" ","");
    }

    @PostMapping(path = "/settleUp")
    public ResponseEntity settleUp(@RequestBody Bill bill) {
        Gson gson = new Gson();
        System.out.println(gson.toJson(bill));
        Optional<Bill> bill1 = billRepositories.findById(bill.getTransactionId());
        int current = bill1.get().getAmountPaid();
        int diff = 0;
        if(bill.isSettled()) {
            bill1.get().setSettled(true);
            diff = bill.getAmountPaid() - current;
        }
        if(bill.isPartial()) {
            bill1.get().setPartial(true);
            diff = bill.getAmountPaid() - current;
        }
        bill1.get().setAmountPaid(bill.getAmountPaid());
        bill1.get().setNextDate(bill.getNextDate());
        billRepositories.save(bill1.get());
        billUpdate.callUpdate(bill1.get());
        settle(bill1.get(), diff);
        com.example.demoApp.demo.dao.Response response = com.example.demoApp.demo.dao.Response.builder().message("settled").build();
        return ResponseEntity.ok(response);
    }
    private void settle(Bill bill, int diff) {
        Optional<Profile> fromProfile = profileRepository.findById(bill.getFromMobileNumber());
        Optional<Profile> toProfile = profileRepository.findById(bill.getToMobileNumber());
        fromProfile.get().setAmountToPay(fromProfile.get().getAmountToPay()-diff);
        toProfile.get().setAmountToGive(toProfile.get().getAmountToGive()-diff);
        List<Profile> profiles = new ArrayList<>();
        profiles.add(fromProfile.get());
        profiles.add(toProfile.get());
        profileRepository.saveAll(profiles);
    }

    @GetMapping(path = "/getfromBill/{frommobileNumber}")
    public List<Bill> getfromBill(@PathVariable String frommobileNumber) {
        List<Bill> bills = billRepositories.findByFromMobileNumber(frommobileNumber);
        return bills;
    }

    @GetMapping(path = "/gettoBill/{tomobileNumber}")
    public List<Bill> getToBill(@PathVariable String toMobileNumber) {
        List<Bill> bills = billRepositories.findByToMobileNumber(toMobileNumber);
        return bills;
    }

    @GetMapping(path = "/getallTransactions/{frommobileNumber}")
    public ResponseEntity<List<Bill>> getTransactions(@PathVariable String frommobileNumber) {
        List<Bill> bills = billRepositories.findByFromMobileNumber(frommobileNumber);
        List<Bill> bills1 = billRepositories.findByToMobileNumber(frommobileNumber);
        bills.addAll(bills1);
        System.out.println(bills);
        Gson gson = new Gson();
        System.out.print(ResponseEntity.ok(bills).getBody().toString());
        return ResponseEntity.ok(bills);
    }

    @GetMapping(path = "/deleteTransactions")
    public ResponseEntity deletetransaction() {
        billRepositories.deleteAll();
        return ResponseEntity.ok("bills");
    }

    @GetMapping(value = "/change/{mobileNumber}")
    public void changemobileNumber(@PathVariable String mobileNumber) {
        Optional<Profile> profile = profileRepository.findById(mobileNumber);
        profile.get().setCreditScore(600);
        profileRepository.save(profile.get());

    }

//    @PostMapping(value = "/update", consumes = "application/json")
//    public ResponseEntity update(@RequestBody Bill bill) {
//        Optional<Bill> bills = billRepositories.findById(bill.getTr());
//        billRepositories.save(bills.get());
//        return ResponseEntity.ok("Updated");
//    }
}
