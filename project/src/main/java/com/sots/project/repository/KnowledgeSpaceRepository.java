package com.sots.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sots.project.model.KnowledgeSpace;

public interface KnowledgeSpaceRepository extends JpaRepository<KnowledgeSpace, Long> {

}
