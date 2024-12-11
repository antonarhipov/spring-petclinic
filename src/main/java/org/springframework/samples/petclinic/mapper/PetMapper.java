package org.springframework.samples.petclinic.mapper;

import org.mapstruct.Mapper;
import org.springframework.samples.petclinic.dto.PetDTO;
import org.springframework.samples.petclinic.owner.Pet;

@Mapper(componentModel = "spring", uses = { PetTypeMapper.class, VisitMapper.class })
public interface PetMapper {

	PetDTO toDto(Pet pet);

	Pet toEntity(PetDTO petDTO);

}
