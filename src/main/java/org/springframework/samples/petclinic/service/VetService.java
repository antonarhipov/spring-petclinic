package org.springframework.samples.petclinic.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VetService {

	private final VetRepository vetRepository;

	public VetService(VetRepository vetRepository) {
		this.vetRepository = vetRepository;
	}

	@Transactional(readOnly = true)
	public List<Vet> findAll() {
		return vetRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Page<Vet> findAll(Pageable pageable) {
		return vetRepository.findAll(pageable);
	}

	@Transactional
	public Vet save(Vet vet) {
		return vetRepository.save(vet);
	}

	@Transactional
	public void delete(Vet vet) {
		vetRepository.delete(vet);
	}

}
