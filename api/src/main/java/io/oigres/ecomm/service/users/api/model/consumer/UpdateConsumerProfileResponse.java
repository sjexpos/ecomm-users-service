package io.oigres.ecomm.service.users.api.model.consumer;

import io.oigres.ecomm.service.users.api.model.UpdateProfileResponse;
import io.oigres.ecomm.service.users.api.model.enums.ConsumerTypeApiEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateConsumerProfileResponse extends UpdateProfileResponse {
    private String firstName;
    private String lastName;
    private String avatar;
    private Long genderId;
    private String phone;
    private String mmjCard;
    private Long zipcodeStateId;
    private Long zipcodeId;
    private ConsumerTypeApiEnum userType;
    private Boolean verified;

    @Builder(builderMethodName = "updateConsumerResponseBuilder")
    public UpdateConsumerProfileResponse(Long id, String email, Boolean isActive, String firstName, String lastName, String avatar, Long genderId, String phone, String mmjCard, Long zipcodeStateId, Long zipcodeId, ConsumerTypeApiEnum userType, Boolean verified) {
        super(id, email, isActive);
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatar = avatar;
        this.genderId = genderId;
        this.phone = phone;
        this.mmjCard = mmjCard;
        this.zipcodeStateId = zipcodeStateId;
        this.zipcodeId = zipcodeId;
        this.userType = userType;
        this.verified = verified;
    }
}
