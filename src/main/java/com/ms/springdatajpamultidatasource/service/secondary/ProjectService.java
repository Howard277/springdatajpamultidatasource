package com.ms.springdatajpamultidatasource.service.secondary;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ms.springdatajpamultidatasource.bean.secondary.Project;
import com.ms.springdatajpamultidatasource.repository.secondary.ProjectRepository;

@Service
@Transactional("transactionManagerSecondary")
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public Project save(Project project) {
        return projectRepository.save(project);
    }

    public void delete(Long id) {
        projectRepository.delete(id);
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }
}
