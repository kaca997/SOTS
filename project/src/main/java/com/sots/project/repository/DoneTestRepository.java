package com.sots.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sots.project.model.DoneTest;
import com.sots.project.model.Student;
import com.sots.project.model.Test;

public interface DoneTestRepository  extends JpaRepository<DoneTest, Long> {

	List<DoneTest> findAllByStudent(Student s);
	List<DoneTest> findAllByTest(Test t);
}
