package org.springframework.samples.petclinic.mapper;

import org.mapstruct.Mapper;
import org.springframework.samples.petclinic.dto.PetTypeDTO;
import org.springframework.samples.petclinic.owner.PetType;

@Mapper(componentModel = "spring")
public interface PetTypeMapper {

	PetTypeDTO toDto(PetType petType);

	PetType toEntity(PetTypeDTO petTypeDTO);

}
