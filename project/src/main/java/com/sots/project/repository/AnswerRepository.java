package com.sots.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sots.project.model.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

}
