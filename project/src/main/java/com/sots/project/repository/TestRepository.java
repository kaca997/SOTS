package com.sots.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sots.project.model.Test;

public interface TestRepository extends JpaRepository<Test, Long> {
	
}
