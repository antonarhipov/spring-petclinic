package org.springframework.samples.petclinic.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.Visit;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * REST controller for managing {@link Visit}s.
 */
@RestController
@RequestMapping("/api/visits")
@Tag(name = "Visit Controller", description = "API for managing visits")
public class VisitRestController {

    private final OwnerRepository ownerRepository;

    public VisitRestController(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    /**
     * Get all visits.
     * @return the list of visits
     */
    @GetMapping
    @Operation(summary = "Get all visits", description = "Returns a list of all visits in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse<List<Visit>>> getAllVisits() {
        List<Visit> visits = new ArrayList<>();

        // Collect all visits from all pets from all owners
        List<Owner> owners = ownerRepository.findAll();
        for (Owner owner : owners) {
            for (Pet pet : owner.getPets()) {
                visits.addAll(pet.getVisits());
            }
        }

        return ResponseEntity.ok(ApiResponse.success("All visits retrieved successfully", visits));
    }

    /**
     * Get a visit by id.
     * @param id the id of the visit to retrieve
     * @return the visit
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get visit by ID", description = "Returns a visit as per the id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @ApiResponse(responseCode = "404", description = "Visit not found",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse<Visit>> getVisit(@PathVariable("id") int id) {
        Visit foundVisit = null;

        // Find the visit with the given id
        List<Owner> owners = ownerRepository.findAll();
        for (Owner owner : owners) {
            for (Pet pet : owner.getPets()) {
                for (Visit visit : pet.getVisits()) {
                    if (visit.getId() != null && visit.getId() == id) {
                        foundVisit = visit;
                        break;
                    }
                }
                if (foundVisit != null) break;
            }
            if (foundVisit != null) break;
        }

        if (foundVisit == null) {
            ApiError error = new ApiError("VISIT_NOT_FOUND", HttpStatus.NOT_FOUND.toString(),
                                         "Visit not found with id: " + id);
            return new ResponseEntity<>(ApiResponse.error("Visit not found", error), HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(ApiResponse.success("Visit retrieved successfully", foundVisit));
    }

    /**
     * Create a new visit for a pet.
     * @param ownerId the id of the owner
     * @param petId the id of the pet
     * @param visit the visit to create
     * @return the created visit
     */
    @PostMapping("/owners/{ownerId}/pets/{petId}")
    @Operation(summary = "Create a new visit", description = "Creates a new visit for a pet")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Visit created",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @ApiResponse(responseCode = "404", description = "Owner or pet not found",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse<Visit>> createVisit(
            @PathVariable("ownerId") int ownerId,
            @PathVariable("petId") int petId,
            @Valid @RequestBody Visit visit) {

        Optional<Owner> optionalOwner = ownerRepository.findById(ownerId);
        if (optionalOwner.isEmpty()) {
            ApiError error = new ApiError("OWNER_NOT_FOUND", HttpStatus.NOT_FOUND.toString(),
                                         "Owner not found with id: " + ownerId);
            return new ResponseEntity<>(ApiResponse.error("Owner not found", error), HttpStatus.NOT_FOUND);
        }

        Owner owner = optionalOwner.get();
        Pet pet = owner.getPet(petId);

        if (pet == null) {
            ApiError error = new ApiError("PET_NOT_FOUND", HttpStatus.NOT_FOUND.toString(),
                                         "Pet not found with id: " + petId);
            return new ResponseEntity<>(ApiResponse.error("Pet not found", error), HttpStatus.NOT_FOUND);
        }

        pet.addVisit(visit);
        ownerRepository.save(owner);

        return new ResponseEntity<>(ApiResponse.success("Visit created successfully", visit), HttpStatus.CREATED);
    }

    /**
     * Update an existing visit.
     * @param id the id of the visit to update
     * @param visitDetails the visit details to update
     * @return the updated visit
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a visit", description = "Updates an existing visit")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Visit updated",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @ApiResponse(responseCode = "404", description = "Visit not found",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse<Visit>> updateVisit(
            @PathVariable("id") int id,
            @Valid @RequestBody Visit visitDetails) {

        Visit foundVisit = null;
        Owner ownerToUpdate = null;

        // Find the visit with the given id
        List<Owner> owners = ownerRepository.findAll();
        for (Owner owner : owners) {
            for (Pet pet : owner.getPets()) {
                for (Visit visit : pet.getVisits()) {
                    if (visit.getId() != null && visit.getId() == id) {
                        foundVisit = visit;
                        ownerToUpdate = owner;
                        break;
                    }
                }
                if (foundVisit != null) break;
            }
            if (foundVisit != null) break;
        }

        if (foundVisit == null || ownerToUpdate == null) {
            ApiError error = new ApiError("VISIT_NOT_FOUND", HttpStatus.NOT_FOUND.toString(),
                                         "Visit not found with id: " + id);
            return new ResponseEntity<>(ApiResponse.error("Visit not found", error), HttpStatus.NOT_FOUND);
        }

        // Update the visit
        foundVisit.setDate(visitDetails.getDate());
        foundVisit.setDescription(visitDetails.getDescription());

        ownerRepository.save(ownerToUpdate);

        return ResponseEntity.ok(ApiResponse.success("Visit updated successfully", foundVisit));
    }

    /**
     * Delete a visit.
     * @param id the id of the visit to delete
     * @return no content
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a visit", description = "Deletes an existing visit")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Visit deleted"),
        @ApiResponse(responseCode = "404", description = "Visit not found",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<Void> deleteVisit(@PathVariable("id") int id) {
        Visit foundVisit = null;
        Owner ownerToUpdate = null;
        Pet petWithVisit = null;

        // Find the visit with the given id
        List<Owner> owners = ownerRepository.findAll();
        for (Owner owner : owners) {
            for (Pet pet : owner.getPets()) {
                for (Visit visit : pet.getVisits()) {
                    if (visit.getId() != null && visit.getId() == id) {
                        foundVisit = visit;
                        ownerToUpdate = owner;
                        petWithVisit = pet;
                        break;
                    }
                }
                if (foundVisit != null) break;
            }
            if (foundVisit != null) break;
        }

        if (foundVisit == null || ownerToUpdate == null || petWithVisit == null) {
            return ResponseEntity.notFound().build();
        }

        // Remove the visit
        petWithVisit.getVisits().remove(foundVisit);
        ownerRepository.save(ownerToUpdate);

        return ResponseEntity.noContent().build();
    }

    /**
     * Get all visits for a pet.
     * @param ownerId the id of the owner
     * @param petId the id of the pet
     * @return the list of visits
     */
    @GetMapping("/owners/{ownerId}/pets/{petId}")
    @Operation(summary = "Get visits for a pet", description = "Returns a list of all visits for a pet")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @ApiResponse(responseCode = "404", description = "Owner or pet not found",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse<Collection<Visit>>> getVisitsForPet(
            @PathVariable("ownerId") int ownerId,
            @PathVariable("petId") int petId) {

        Optional<Owner> optionalOwner = ownerRepository.findById(ownerId);
        if (optionalOwner.isEmpty()) {
            ApiError error = new ApiError("OWNER_NOT_FOUND", HttpStatus.NOT_FOUND.toString(),
                                         "Owner not found with id: " + ownerId);
            return new ResponseEntity<>(ApiResponse.error("Owner not found", error), HttpStatus.NOT_FOUND);
        }

        Owner owner = optionalOwner.get();
        Pet pet = owner.getPet(petId);

        if (pet == null) {
            ApiError error = new ApiError("PET_NOT_FOUND", HttpStatus.NOT_FOUND.toString(),
                                         "Pet not found with id: " + petId);
            return new ResponseEntity<>(ApiResponse.error("Pet not found", error), HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(ApiResponse.success("Visits retrieved successfully", pet.getVisits()));
    }
}
