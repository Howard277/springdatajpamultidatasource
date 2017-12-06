package com.ms.springdatajpamultidatasource.controller.multiple;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ms.springdatajpamultidatasource.bean.primary.User;
import com.ms.springdatajpamultidatasource.service.primary.UserService;

@RestController
@RequestMapping("/multiple/user")
public class MultipleUserController {

    @Autowired
    private UserService primaryUserService;

    @Autowired
    private com.ms.springdatajpamultidatasource.service.secondary.UserService secondaryUserService;

    @RequestMapping(path = "save", method = RequestMethod.POST)
    public void save() {
        User primaryUser = new User(null, "primaryUser", "primaryUser");
        primaryUserService.save(primaryUser);
        com.ms.springdatajpamultidatasource.bean.secondary.User secondaryUser = new com.ms.springdatajpamultidatasource.bean.secondary.User(
                null, "secondaryUser", "secondaryUser", new Date());
        secondaryUserService.save(secondaryUser);
    }
}
