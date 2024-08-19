package io.oigres.ecomm.service.users.api.model.admin;

import io.oigres.ecomm.service.users.api.model.UpdateProfileRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
public class UpdateAdminProfileRequest extends UpdateProfileRequest {
    @Schema(name = "firstName", example = "SomeName", required = true)
    @NotEmpty
    private String firstName;
    @Schema(name = "lastName", example = "SomeLastName", required = true)
    @NotEmpty
    private String lastName;
    @Schema(name = "img", example = "The image URL", required = false)
    private String img;
    @Schema(name = "phone", example = "Phone number. Just the numbers", required = true)
    @NotEmpty
    private String phone;
    @Schema(name = "email", example = "The email.", required = true)
    @NotEmpty
    @Email
    private String email;


}
