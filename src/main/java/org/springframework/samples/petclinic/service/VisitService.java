package org.springframework.samples.petclinic.service;

import java.util.List;

import org.springframework.samples.petclinic.owner.Visit;
import org.springframework.samples.petclinic.owner.VisitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VisitService {

	private final VisitRepository visitRepository;

	public VisitService(VisitRepository visitRepository) {
		this.visitRepository = visitRepository;
	}

	@Transactional(readOnly = true)
	public Visit findById(int id) {
		return visitRepository.findById(id).orElse(null);
	}

	@Transactional
	public Visit save(Visit visit) {
		return visitRepository.save(visit);
	}

	@Transactional
	public void delete(Visit visit) {
		visitRepository.delete(visit);
	}

}
