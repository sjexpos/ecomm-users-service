package io.oigres.ecomm.service.users.api.model;

import java.io.Serializable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ValidateUserRequest implements Serializable {
    @NotNull 
    @NotEmpty
    @Email
    private String email;
    @NotNull 
    @NotEmpty
    private String password;

}
