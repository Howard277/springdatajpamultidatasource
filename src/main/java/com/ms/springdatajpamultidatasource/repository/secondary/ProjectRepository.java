package com.ms.springdatajpamultidatasource.repository.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ms.springdatajpamultidatasource.bean.secondary.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

}
