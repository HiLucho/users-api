package com.luis.project.cl.users.infrastructure.adapter.out.persistence.repository;

import com.luis.project.cl.users.infrastructure.adapter.out.persistence.dto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * User Repository.
 */
@Repository
public interface UserRepository extends JpaRepository<UserDto, UUID> {

    Optional<UserDto> findByEmail(String email);
}
