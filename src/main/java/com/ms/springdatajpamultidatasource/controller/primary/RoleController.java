package com.ms.springdatajpamultidatasource.controller.primary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ms.springdatajpamultidatasource.bean.primary.Role;
import com.ms.springdatajpamultidatasource.service.primary.RoleService;

@RestController
@RequestMapping("/primary/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @RequestMapping(path = "save", method = RequestMethod.POST)
    public Role save(@RequestBody Role role) {
        return roleService.save(role);
    }
}
