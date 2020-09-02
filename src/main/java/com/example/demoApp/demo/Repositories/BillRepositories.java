package com.example.demoApp.demo.Repositories;

import com.example.demoApp.demo.dao.Bill;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BillRepositories extends CrudRepository<Bill, String> {
    List<Bill> findByFromMobileNumber(String fromMobileNumber);
    List<Bill> findByToMobileNumber(String toMobileNumber);
//    @Query()
//    List<Bill> findByTransactionId(Long transactionId);
    List<Bill> findByToMobileNumberAndFromMobileNumber(String toMobileNumber, String fromMobileNumber);
}
