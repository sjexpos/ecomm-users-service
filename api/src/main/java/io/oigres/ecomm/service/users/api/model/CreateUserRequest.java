package io.oigres.ecomm.service.users.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateUserRequest implements Serializable {

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String password;

}
