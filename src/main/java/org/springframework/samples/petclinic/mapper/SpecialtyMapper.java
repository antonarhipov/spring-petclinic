package org.springframework.samples.petclinic.mapper;

import org.mapstruct.Mapper;
import org.springframework.samples.petclinic.dto.SpecialtyDTO;
import org.springframework.samples.petclinic.vet.Specialty;

@Mapper(componentModel = "spring")
public interface SpecialtyMapper {

	SpecialtyDTO toDto(Specialty specialty);

	Specialty toEntity(SpecialtyDTO specialtyDTO);

}
