package com.ukeess.bootstrap.web;

import com.ukeess.bootstrap.web.model.Account;
import com.ukeess.bootstrap.web.repository.AccountRepo;
import com.ukeess.bootstrap.web.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountRepo accountRepo;

    @Override
    public List<Account> findAll() {
        return accountRepo.findAll();
    }

}
