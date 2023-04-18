package com.luis.project.cl.users.domain.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class PhoneRequest {
    private UUID id;
    @NotEmpty(message = "Number cannot be empty")
    private String number;
    @NotEmpty(message = "CityCode cannot be empty")
    private String cityCode;
    @NotEmpty(message = "CountryCode cannot be empty")
    private String countryCode;
}
