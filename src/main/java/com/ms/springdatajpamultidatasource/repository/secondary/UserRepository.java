package com.ms.springdatajpamultidatasource.repository.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ms.springdatajpamultidatasource.bean.secondary.User;

@Repository(value = "secondaryUserRepository")
public interface UserRepository extends JpaRepository<User, Long> {

}
