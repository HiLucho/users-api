package com.luis.project.cl.users.infrastructure.adapter.out.persistence.dto;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "phones")
public class PhoneDto {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String number;
    @Column(nullable = false, name = "city_code")
    private String cityCode;
    @Column(nullable = false, name = "country_code")
    private String countryCode;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private UserDto user;
}
