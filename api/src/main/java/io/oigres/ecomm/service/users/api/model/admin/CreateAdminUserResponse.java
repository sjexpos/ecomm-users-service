package io.oigres.ecomm.service.users.api.model.admin;

import io.oigres.ecomm.service.users.api.model.CreateUserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateAdminUserResponse extends CreateUserResponse {

    private String firstName;
    private String lastName;
    private String avatar;
    private String phone;
    private Boolean isActive;

    @Builder(builderMethodName = "adminResponseBuilder")
    public CreateAdminUserResponse(Long id, String email, String firstName, String lastName, String avatar, String phone, Boolean isActive) {
        super(id, email);
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatar = avatar;
        this.phone = phone;
        this.isActive = isActive;
    }
}
