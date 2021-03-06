package com.sots.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sots.project.model.Domain;

public interface DomainRepository extends JpaRepository<Domain, Long> {

}
