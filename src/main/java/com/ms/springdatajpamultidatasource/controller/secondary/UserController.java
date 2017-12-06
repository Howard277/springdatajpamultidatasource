package com.ms.springdatajpamultidatasource.controller.secondary;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ms.springdatajpamultidatasource.bean.secondary.User;
import com.ms.springdatajpamultidatasource.service.secondary.UserService;

@RestController("secondaryUserController")
@RequestMapping("/secondary/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(path = "saveList", method = RequestMethod.POST)
    public List<User> saveList() {
        List<User> list = new ArrayList<User>();
        list.add(new User(null, "1111", "1111", new Date()));
        list.add(new User(null, "222", "222", new Date()));
        return userService.save(list);
    }

    @RequestMapping(path = "saveList2", method = RequestMethod.POST)
    public List<User> saveList2() {
        List<User> list = new ArrayList<User>();
        list.add(new User(null, "333", "333", new Date()));
        list.add(new User(null, "333", "333", new Date()));
        return userService.save(list);
    }
}
