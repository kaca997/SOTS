package com.sots.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sots.project.model.KnowledgeSpaceDomain;

public interface KnowledgeSpaceRepository extends JpaRepository<KnowledgeSpaceDomain, Long> {

}
