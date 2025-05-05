package org.springframework.samples.petclinic.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.samples.petclinic.model.Person;

/**
 * Simple JavaBean domain object representing a user.
 */
@Entity
@Table(name = "users")
public class User extends Person {

    @Column(name = "username", unique = true)
    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @Column(name = "password")
    @NotBlank
    @Size(min = 6)
    private String password;

    @Column(name = "email", unique = true)
    @NotBlank
    private String email;

    @Column(name = "role")
    @NotBlank
    private String role;

    @Column(name = "enabled")
    private boolean enabled = true;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
