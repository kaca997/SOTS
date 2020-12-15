package com.sots.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sots.project.model.Domain;
import com.sots.project.model.Problem;

public interface ProblemRepository extends JpaRepository<Problem, Long> {

	@Query(value = "SELECT pr.* FROM problem pr WHERE pr.name = ?1 AND pr.domain_id = ?2", nativeQuery = true)
	Problem findByNameAndDomain(String name, Long domainID);
}
