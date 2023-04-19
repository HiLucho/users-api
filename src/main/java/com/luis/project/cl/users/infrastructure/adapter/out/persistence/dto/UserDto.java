package com.luis.project.cl.users.infrastructure.adapter.out.persistence.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class UserDto {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, name = "FULL_NAME")
    private String fullName;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private LocalDateTime created;
    private LocalDateTime modified;
    @Column(nullable = false, name = "last_login")
    private LocalDateTime lastLogin;
    @Column(nullable = false)
    private UUID token;
    @Column(nullable = false, name = "is_active")
    private boolean isActive;
    @OneToMany(mappedBy = "user", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private List<PhoneDto> phones;
}
