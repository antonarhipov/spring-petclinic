package org.springframework.samples.petclinic.mapper;

import org.mapstruct.Mapper;
import org.springframework.samples.petclinic.dto.VisitDTO;
import org.springframework.samples.petclinic.owner.Visit;

@Mapper(componentModel = "spring")
public interface VisitMapper {

	VisitDTO toDto(Visit visit);

	Visit toEntity(VisitDTO visitDTO);

}
