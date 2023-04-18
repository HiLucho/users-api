package com.luis.project.cl.users.infrastructure.adapter.out.persistence.repository;

import com.luis.project.cl.users.infrastructure.adapter.out.persistence.dto.PhoneDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Phone Repository.
 */
@Repository
public interface PhoneRepository extends JpaRepository<PhoneDto, String> {

}
