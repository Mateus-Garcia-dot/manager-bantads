package com.bantads.manager.repository;

import com.bantads.manager.model.ManagerModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<ManagerModel, Long> {
}
