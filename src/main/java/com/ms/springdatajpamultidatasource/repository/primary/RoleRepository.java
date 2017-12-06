package com.ms.springdatajpamultidatasource.repository.primary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ms.springdatajpamultidatasource.bean.primary.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
