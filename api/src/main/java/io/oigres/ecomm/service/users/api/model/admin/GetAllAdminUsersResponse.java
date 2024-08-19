package io.oigres.ecomm.service.users.api.model.admin;

import io.oigres.ecomm.service.users.api.model.GetAllUsersResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
public class GetAllAdminUsersResponse extends GetAllUsersResponse {
    private String firstName;
    private String lastName;
    private String avatar;
    private String phone;
    private Boolean isActive;

    @Builder(builderMethodName = "getAllAdminResponseBuilder")
    public GetAllAdminUsersResponse(Long userId, String email, String firstName, String lastName, String avatar, String phone, Boolean isActive) {
        super(userId, email);
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatar = avatar;
        this.phone = phone;
        this.isActive = isActive;
    }
}
