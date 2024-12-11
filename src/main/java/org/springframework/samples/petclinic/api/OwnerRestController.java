package org.springframework.samples.petclinic.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.dto.OwnerDTO;
import org.springframework.samples.petclinic.mapper.OwnerMapper;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/owners")
@CrossOrigin
public class OwnerRestController {

	private final OwnerService ownerService;

	private final OwnerMapper ownerMapper;

	public OwnerRestController(OwnerService ownerService, OwnerMapper ownerMapper) {
		this.ownerService = ownerService;
		this.ownerMapper = ownerMapper;
	}

	@GetMapping("/{ownerId}")
	public ResponseEntity<OwnerDTO> getOwner(@PathVariable("ownerId") int ownerId) {
		Owner owner = ownerService.findById(ownerId);
		return owner != null ? ResponseEntity.ok(ownerMapper.toDto(owner)) : ResponseEntity.notFound().build();
	}

	@GetMapping("/*/lastname/{lastName}")
	public ResponseEntity<Page<OwnerDTO>> getOwnersList(@PathVariable("lastName") String lastName, Pageable pageable) {
		Page<Owner> owners = ownerService.findByLastName(lastName, pageable);
		Page<OwnerDTO> dtos = owners.map(ownerMapper::toDto);
		return ResponseEntity.ok(dtos);
	}

	@PostMapping
	public ResponseEntity<OwnerDTO> createOwner(@Valid @RequestBody OwnerDTO ownerDTO) {
		Owner owner = ownerMapper.toEntity(ownerDTO);
		ownerService.save(owner);
		return ResponseEntity.created(URI.create("/api/owners/" + owner.getId())).body(ownerMapper.toDto(owner));
	}

	@PutMapping("/{ownerId}")
	public ResponseEntity<OwnerDTO> updateOwner(@PathVariable("ownerId") int ownerId,
			@Valid @RequestBody OwnerDTO ownerDTO) {
		Owner currentOwner = ownerService.findById(ownerId);
		if (currentOwner == null) {
			return ResponseEntity.notFound().build();
		}
		Owner owner = ownerMapper.toEntity(ownerDTO);
		owner.setId(ownerId);
		ownerService.save(owner);
		return ResponseEntity.ok(ownerMapper.toDto(owner));
	}

	@DeleteMapping("/{ownerId}")
	public ResponseEntity<Void> deleteOwner(@PathVariable("ownerId") int ownerId) {
		Owner owner = ownerService.findById(ownerId);
		if (owner == null) {
			return ResponseEntity.notFound().build();
		}
		ownerService.delete(owner);
		return ResponseEntity.noContent().build();
	}

}
