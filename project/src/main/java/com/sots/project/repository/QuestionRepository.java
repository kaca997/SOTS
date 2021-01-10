package com.sots.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sots.project.model.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {

}
