package com.example.demoApp.demo.Repositories;

import com.example.demoApp.demo.dao.Bill;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BillRepositories extends CrudRepository<Bill, Long> {
    List<Bill> findByFromMobileNumber(String fromMobileNumber);
    List<Bill> findByToMobileNumber(String toMobileNumber);
}
