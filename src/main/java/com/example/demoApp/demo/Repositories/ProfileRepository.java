package com.example.demoApp.demo.Repositories;

import com.example.demoApp.demo.dao.Profile;
import org.springframework.data.repository.CrudRepository;

public interface ProfileRepository extends CrudRepository<Profile, String> {
    Profile findByMobileNumber(String mobileNumber);
}
