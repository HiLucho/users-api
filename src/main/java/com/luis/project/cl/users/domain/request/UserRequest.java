package com.luis.project.cl.users.domain.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    @NotEmpty(message = "Name cannot be empty")
    private String name;
    @Pattern(regexp = "^[^@]+@dominio\\.cl$", message = "Email must be a valid email address. Ex. user@dominio.cl")
    private String email;
    @NotEmpty(message = "Password cannot be empty")
    private String password;
    @NotNull(message = "Phones cannot be empty")
    private List<PhoneRequest> phones;
}
