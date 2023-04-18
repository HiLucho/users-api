package com.luis.project.cl.users.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Phone {
    private UUID id;
    private String number;
    private String cityCode;
    private String countryCode;
}
