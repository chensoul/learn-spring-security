package com.chensoul.lss.service;

import com.chensoul.lss.persistence.model.Project;
import java.util.Optional;

public interface IProjectService {

  Optional<Project> findById(Long id);

  Project save(Project project);

  Iterable<Project> findAll();

}
