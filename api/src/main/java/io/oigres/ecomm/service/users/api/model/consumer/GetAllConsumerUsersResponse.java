package io.oigres.ecomm.service.users.api.model.consumer;

import io.oigres.ecomm.service.users.api.model.GetAllUsersResponse;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetAllConsumerUsersResponse extends GetAllUsersResponse {
    private String firstName;
    private String lastName;
    private String avatar;
    private String gender;
    private String phone;
    private String state;
    private String zipCode;
    private String userType;
    private Boolean isActive;
    private Boolean verified;

    @Builder(builderMethodName = "getAllConsumerResponseBuilder")
    public GetAllConsumerUsersResponse(Long userId,
                                       String email,
                                       String firstName,
                                       String lastName,
                                       String avatar,
                                       String gender,
                                       String phone,
                                       String state,
                                       String zipCode,
                                       String userType,
                                       Boolean isActive,
                                       Boolean verified) {
        super(userId, email);
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatar = avatar;
        this.gender = gender;
        this.phone = phone;
        this.state = state;
        this.zipCode = zipCode;
        this.userType = userType;
        this.isActive = isActive;
        this.verified = verified;
    }
}
