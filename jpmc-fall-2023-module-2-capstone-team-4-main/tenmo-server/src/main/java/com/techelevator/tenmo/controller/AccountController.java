package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.exception.DaoException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
@RestController
public class AccountController {

    private final JdbcAccountDao accountDao;
    public AccountController(JdbcAccountDao accountDao) {
        this.accountDao = accountDao;
    }



    @RequestMapping(path = "/account/{user_id}", method = RequestMethod.GET)
    public BigDecimal balance(@PathVariable int user_id){
        return accountDao.getBalanceByID(user_id);
    }
}
