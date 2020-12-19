package com.sots.project.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sots.project.model.Test;

public interface TestRepository extends JpaRepository<Test, Long> {
	@Query(value = "select * from test t where t.course_id in(select c.id from course c where c.domain_id = ?1)", nativeQuery = true)
	List<Test> findByDomain(Long domainID);
}
