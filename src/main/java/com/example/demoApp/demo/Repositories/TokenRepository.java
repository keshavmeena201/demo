package com.example.demoApp.demo.Repositories;

import com.example.demoApp.demo.dao.Token;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<Token, String> {
}
