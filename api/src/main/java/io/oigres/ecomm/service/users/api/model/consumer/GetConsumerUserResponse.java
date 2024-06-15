package io.oigres.ecomm.service.users.api.model.consumer;

import io.oigres.ecomm.service.users.api.model.GetUserResponse;
import io.oigres.ecomm.service.users.api.model.enums.ConsumerTypeApiEnum;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetConsumerUserResponse extends GetUserResponse {
    private String firstName;
    private String lastName;
    private String avatar;
    private Long genderId;
    private String phone;
    private String mmjCard;
    private Long zipcodeStateId;
    private Long zipcodeId;
    private ConsumerTypeApiEnum userType;
    private Boolean isActive;
    private Boolean verified;

    @Builder(builderMethodName = "getConsumerResponseBuilder")
    public GetConsumerUserResponse(Long id, String email, String firstName, String lastName, String avatar, Long genderId, String phone, String mmjCard, Long zipcodeStateId, Long zipcodeId, ConsumerTypeApiEnum userType, Boolean isActive, Boolean verified) {
        super(id, email);
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatar = avatar;
        this.genderId = genderId;
        this.phone = phone;
        this.mmjCard = mmjCard;
        this.zipcodeStateId = zipcodeStateId;
        this.zipcodeId = zipcodeId;
        this.userType = userType;
        this.isActive = isActive;
        this.verified = verified;
    }
}
