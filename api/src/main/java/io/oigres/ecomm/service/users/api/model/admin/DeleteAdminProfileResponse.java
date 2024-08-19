package io.oigres.ecomm.service.users.api.model.admin;

import io.oigres.ecomm.service.users.api.model.DeleteProfileResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
public class DeleteAdminProfileResponse extends DeleteProfileResponse {
    private String firstName;
    private String lastName;
    private String avatar;
    private String phone;

    @Builder(builderMethodName = "deleteAdminResponseBuilder")
    public DeleteAdminProfileResponse(Long id, String email, Boolean isActive, String firstName, String lastName, String avatar, String phone) {
        super(id, email, isActive);
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatar = avatar;
        this.phone = phone;
    }
}
