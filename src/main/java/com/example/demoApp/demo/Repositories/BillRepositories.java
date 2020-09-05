package com.example.demoApp.demo.Repositories;

import com.example.demoApp.demo.dao.Bill;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BillRepositories extends CrudRepository<Bill, String> {
    List<Bill> findByFromMobileNumber(String fromMobileNumber);
    List<Bill> findByToMobileNumber(String toMobileNumber);
//    @Query("SELECT * FROM Bill b WHERE b.fromMobileNumber == :mobileNumber AND b.isSettled == false")
//    List<Bill> findByUnSettled(@Param("mobileNumber") String mobileNumber);
    List<Bill> findByToMobileNumberAndFromMobileNumber(String toMobileNumber, String fromMobileNumber);
}
