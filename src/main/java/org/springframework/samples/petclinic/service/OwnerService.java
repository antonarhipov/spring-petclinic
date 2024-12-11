package org.springframework.samples.petclinic.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OwnerService {

	private final OwnerRepository ownerRepository;

	public OwnerService(OwnerRepository ownerRepository) {
		this.ownerRepository = ownerRepository;
	}

	@Transactional(readOnly = true)
	public Owner findById(int id) {
		return ownerRepository.findById(id).orElse(null);
	}

	@Transactional(readOnly = true)
	public Page<Owner> findByLastName(String lastName, Pageable pageable) {
		return ownerRepository.findByLastName(lastName, pageable);
	}

	@Transactional
	public Owner save(Owner owner) {
		return ownerRepository.save(owner);
	}

	@Transactional
	public void delete(Owner owner) {
		ownerRepository.delete(owner);
	}

}
