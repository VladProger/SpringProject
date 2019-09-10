package com.ukeess.bootstrap.web.repository;


import com.ukeess.bootstrap.web.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepo extends JpaRepository<Account, Integer> {

}
