package io.oigres.ecomm.service.users.api.model.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import io.oigres.ecomm.service.users.api.model.CreateUserRequest;


@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
public class CreateAdminUserRequest extends CreateUserRequest {
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;

    private String avatar;

    @NotEmpty
    private String phone;

    @Builder(builderMethodName = "adminBuilder")
    public CreateAdminUserRequest(@NotEmpty @Email String email, @NotEmpty String password, String firstName, String lastName, String avatar, String phone) {
        super(email, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatar = avatar;
        this.phone = phone;
    }
}
