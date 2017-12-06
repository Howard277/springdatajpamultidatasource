package com.ms.springdatajpamultidatasource.service.primary;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ms.springdatajpamultidatasource.bean.primary.User;
import com.ms.springdatajpamultidatasource.repository.primary.UserRepository;

@Service("primaryUserService")
@Transactional("transactionManagerPrimary")
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    public List<User> save(List<User> list) {
        return userRepository.save(list);
    }

    public void delete(Long id) {
        userRepository.delete(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
