package org.springframework.samples.petclinic.user;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository class for <code>User</code> domain objects
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Retrieve a {@link User} from the data store by username.
     * @param username the username to search for
     * @return the {@link User} if found
     */
    Optional<User> findByUsername(String username);

    /**
     * Retrieve a {@link User} from the data store by email.
     * @param email the email to search for
     * @return the {@link User} if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if a username exists in the data store.
     * @param username the username to check
     * @return true if the username exists
     */
    boolean existsByUsername(String username);

    /**
     * Check if an email exists in the data store.
     * @param email the email to check
     * @return true if the email exists
     */
    boolean existsByEmail(String email);

    /**
     * Returns all the users from data store with pagination
     */
    Page<User> findAll(Pageable pageable);
}
