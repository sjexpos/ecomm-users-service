package io.oigres.ecomm.service.users.api.model.consumer;

import io.oigres.ecomm.service.users.api.model.CreateUserResponse;
import io.oigres.ecomm.service.users.api.model.enums.ConsumerTypeApiEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateConsumerUserResponse extends CreateUserResponse {

    private String firstName;
    private String lastName;
    private String avatar;
    private Long genderId;
    private String phone;
    private String cardImageURL;
    private Long zipcodeStateId;
    private Long zipcodeId;
    private ConsumerTypeApiEnum userType;
    private Boolean isActive;
    private Boolean verified;

    @Builder(builderMethodName = "consumerResponseBuilder")
    public CreateConsumerUserResponse(Long id, String email, String firstName, String lastName, String avatar, Long genderId, String phone, String cardImageURL, Long zipcodeStateId, Long zipcodeId, ConsumerTypeApiEnum userType, Boolean isActive, Boolean verified) {
        super(id, email);
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatar = avatar;
        this.genderId = genderId;
        this.phone = phone;
        this.cardImageURL = cardImageURL;
        this.zipcodeStateId = zipcodeStateId;
        this.zipcodeId = zipcodeId;
        this.userType = userType;
        this.isActive = isActive;
        this.verified = verified;
    }
}
