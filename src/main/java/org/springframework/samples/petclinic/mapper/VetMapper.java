package org.springframework.samples.petclinic.mapper;

import org.mapstruct.Mapper;
import org.springframework.samples.petclinic.dto.VetDTO;
import org.springframework.samples.petclinic.vet.Vet;

@Mapper(componentModel = "spring", uses = { SpecialtyMapper.class })
public interface VetMapper {

	VetDTO toDto(Vet vet);

	Vet toEntity(VetDTO vetDTO);

}
