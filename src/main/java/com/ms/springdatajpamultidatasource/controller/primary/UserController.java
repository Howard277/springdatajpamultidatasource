package com.ms.springdatajpamultidatasource.controller.primary;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ms.springdatajpamultidatasource.bean.primary.User;
import com.ms.springdatajpamultidatasource.service.primary.UserService;

@RestController("primaryUserController")
@RequestMapping(path = "/primary/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(path = "saveList", method = RequestMethod.POST)
    public List<User> saveList() {
        List<User> list = new ArrayList<User>();
        list.add(new User(null, "zhangsan", "zhangsan"));
        list.add(new User(null, "listi", "lisi"));
        return userService.save(list);
    }

    @RequestMapping(path = "saveList2", method = RequestMethod.POST)
    public List<User> saveList2() {
        List<User> list = new ArrayList<User>();
        list.add(new User(null, "wangwu", "wangwu"));
        list.add(new User(null, "wangwu", "wangwu"));
        return userService.save(list);
    }
}
