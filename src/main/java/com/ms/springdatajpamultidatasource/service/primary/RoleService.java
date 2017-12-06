package com.ms.springdatajpamultidatasource.service.primary;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ms.springdatajpamultidatasource.bean.primary.Role;
import com.ms.springdatajpamultidatasource.repository.primary.RoleRepository;

@Service
@Transactional("transactionManagerPrimary")
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role save(Role role) {
        return roleRepository.save(role);
    }

    public void delete(Role role) {
        roleRepository.delete(role);
    }

    public void delete(Long id) {
        roleRepository.delete(id);
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}
