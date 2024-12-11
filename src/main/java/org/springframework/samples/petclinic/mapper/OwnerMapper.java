package org.springframework.samples.petclinic.mapper;

import org.mapstruct.Mapper;
import org.springframework.samples.petclinic.dto.OwnerDTO;
import org.springframework.samples.petclinic.owner.Owner;

@Mapper(componentModel = "spring", uses = { PetMapper.class })
public interface OwnerMapper {

	OwnerDTO toDto(Owner owner);

	Owner toEntity(OwnerDTO ownerDTO);

}
