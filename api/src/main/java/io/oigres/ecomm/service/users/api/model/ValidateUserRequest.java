package io.oigres.ecomm.service.users.api.model;

import java.io.Serializable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotEmpty;

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
