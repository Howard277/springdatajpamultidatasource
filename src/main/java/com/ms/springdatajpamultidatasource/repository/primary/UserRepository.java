package com.ms.springdatajpamultidatasource.repository.primary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ms.springdatajpamultidatasource.bean.primary.User;

@Repository(value = "primaryUserRepository")
public interface UserRepository extends JpaRepository<User, Long> {

}
