package com.sots.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sots.project.model.Course;

public interface CourseRepository extends JpaRepository<Course, Long>{

}
