package org.springframework.samples.petclinic.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PetDTO {

	private Integer id;

	private String name;

	private LocalDate birthDate;

	private PetTypeDTO type;

	private List<VisitDTO> visits = new ArrayList<>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public PetTypeDTO getType() {
		return type;
	}

	public void setType(PetTypeDTO type) {
		this.type = type;
	}

	public List<VisitDTO> getVisits() {
		return visits;
	}

	public void setVisits(List<VisitDTO> visits) {
		this.visits = visits;
	}

}
