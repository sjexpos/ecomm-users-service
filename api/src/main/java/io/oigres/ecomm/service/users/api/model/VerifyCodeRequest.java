package io.oigres.ecomm.service.users.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VerifyCodeRequest implements Serializable {
    @NotNull 
    @NotEmpty
    @Email
    private String email;

    @NotNull
    @NotEmpty
    private String code;
}
