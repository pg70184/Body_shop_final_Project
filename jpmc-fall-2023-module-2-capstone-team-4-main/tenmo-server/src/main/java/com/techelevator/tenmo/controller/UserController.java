package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.techelevator.tenmo.dao.JdbcUserDao;

import java.util.List;

@RestController
public class UserController {

    private final JdbcUserDao userDao;

    public UserController(JdbcUserDao userDao){
        this.userDao = userDao;
    }

    @RequestMapping(path = "/user", method = RequestMethod.GET)
    public List<User> users(){
        return userDao.getUsers();
    }

}
