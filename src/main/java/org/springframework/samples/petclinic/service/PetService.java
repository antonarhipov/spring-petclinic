package org.springframework.samples.petclinic.service;

import java.util.List;
import java.util.Optional;

import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetRepository;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PetService {

	private final PetRepository petRepository;

	public PetService(PetRepository petRepository) {
		this.petRepository = petRepository;
	}

	@Transactional(readOnly = true)
	public List<PetType> findPetTypes() {
		return petRepository.findPetTypes();
	}

	@Transactional(readOnly = true)
	public Optional<Pet> findById(int id) {
		return petRepository.findById(id);
	}

	@Transactional
	public Pet save(Pet pet) {
		return petRepository.save(pet);
	}

	@Transactional
	public void delete(Pet pet) {
		petRepository.delete(pet);
	}

}
