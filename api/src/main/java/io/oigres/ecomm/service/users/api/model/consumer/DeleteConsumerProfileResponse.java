package io.oigres.ecomm.service.users.api.model.consumer;

import io.oigres.ecomm.service.users.api.model.DeleteProfileResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeleteConsumerProfileResponse extends DeleteProfileResponse {
    private String firstName;
    private String lastName;

    private String avatar;
    private String gender;
    private String phone;
    private String mmjCard;
    private String state;
    private String zipCode;
    private String userType;
    private Boolean isActive;
    private Boolean verified;

    @Builder(builderMethodName = "deleteConsumerResponseBuilder")
    public DeleteConsumerProfileResponse(Long id, String email, Boolean isActive, String firstName, String lastName, String avatar, String gender, String phone, String mmjCard, String state, String zipCode, String userType, Boolean isActive1, Boolean verified) {
        super(id, email, isActive);
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatar = avatar;
        this.gender = gender;
        this.phone = phone;
        this.mmjCard = mmjCard;
        this.state = state;
        this.zipCode = zipCode;
        this.userType = userType;
        this.isActive = isActive1;
        this.verified = verified;
    }
}
