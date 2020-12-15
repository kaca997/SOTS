package com.sots.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sots.project.dto.NewDomainDTO;
import com.sots.project.model.Domain;
import com.sots.project.model.KnowledgeSpace;
import com.sots.project.model.KnowledgeSpaceType;
import com.sots.project.repository.CourseRepository;
import com.sots.project.repository.DomainRepository;

@Service
public class DomainService {

	@Autowired
	private DomainRepository domainRepository;
//	
//	@Autowired
//	private KnowledgeSpace ksRepository;
//	
//	@Autowired
//	private CourseRepository courseRepository;
	
	public String save(NewDomainDTO domainDTO) {
		Domain domain = new Domain();
		domain.setName(domainDTO.getDomainName());
//		
//		KnowledgeSpace ks = new KnowledgeSpace();
//		ks.setDomain(domain);
//		ks.setKnowledgeSpaceType(KnowledgeSpaceType.EXPECTED);
//		
//		for(String problem : domainDTO.getProblemList()) {
//			domain
//		}
		return null;
	}
}
