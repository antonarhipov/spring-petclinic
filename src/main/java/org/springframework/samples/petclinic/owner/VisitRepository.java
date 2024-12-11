package org.springframework.samples.petclinic.owner;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Integer> {

	/**
	 * Retrieve a {@link Visit} from the data store by id.
	 * @param id the id to search for
	 * @return the {@link Visit} if found
	 */
	@Query("SELECT visit FROM Visit visit WHERE visit.id =:id")
	@Transactional(readOnly = true)
	Optional<Visit> findById(@Param("id") Integer id);

	/**
	 * Find visits by pet id.
	 * @param petId the pet id to search for
	 * @return a Collection of matching visits
	 */
	@Query("SELECT visit FROM Visit visit WHERE visit.pet.id = :petId")
	@Transactional(readOnly = true)
	List<Visit> findByPetId(@Param("petId") Integer petId);

}
