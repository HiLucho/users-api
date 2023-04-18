package com.luis.project.cl.users.infrastructure.adapter.out.persistence.dto;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "phone")
public class PhoneDto {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String number;
    @Column(nullable = false, name= "CITYCODE")
    private String cityCode;
    @Column(nullable = false, name= "COUNTRYCODE")
    private String countryCode;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", nullable = false)
    private UserDto user;
}
