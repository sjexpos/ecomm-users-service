package io.oigres.ecomm.service.users.api.model.admin;

import io.oigres.ecomm.service.users.api.model.UpdateProfileResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
public class UpdateAdminProfileResponse extends UpdateProfileResponse {
    private String firstName;
    private String lastName;
    private String img;
    private String phone;

    @Builder(builderMethodName = "updateAdminResponseBuilder")
    public UpdateAdminProfileResponse(Long id, String email, Boolean isActive, String firstName, String lastName, String img, String phone) {
        super(id, email, isActive);
        this.firstName = firstName;
        this.lastName = lastName;
        this.img = img;
        this.phone = phone;
    }
}
