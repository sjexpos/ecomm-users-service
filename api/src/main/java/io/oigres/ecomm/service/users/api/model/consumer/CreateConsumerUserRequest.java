package io.oigres.ecomm.service.users.api.model.consumer;

import io.oigres.ecomm.service.users.api.model.CreateUserRequest;
import io.oigres.ecomm.service.users.api.model.enums.ConsumerTypeApiEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
public class CreateConsumerUserRequest extends CreateUserRequest {
    @Schema(name = "firstName", example = "", required = true)
    @NotEmpty
    private String firstName;
    @Schema(name = "lastName", example = "", required = true)
    @NotEmpty
    private String lastName;
    @Schema(name = "avatar", example = "", required = false)
    private String avatar;
    @Schema(name = "cardImageURL", example = "", required = false)
    @NotNull
    private String cardImageURL;
    @Schema(name = "userType", example = "RECREATIONAL", required = true)
    @NotNull
    private ConsumerTypeApiEnum userType;
    @Schema(name = "genderId", example = "", required = true)
    @NotNull
    private Long genderId;
    @Schema(name = "phone", example = "", required = true)
    @NotEmpty
    private String phone;
    @Schema(name = "zipcodeStateId", example = "", required = true)
    @NotNull
    private Long zipcodeStateId;
    @Schema(name = "zipcodeId", example = "", required = true)
    @NotNull
    private Long zipcodeId;

    @Builder(builderMethodName = "consumerBuilder")
    public CreateConsumerUserRequest(@NotEmpty @Email String email, @NotEmpty String password, String firstName, String lastName, String avatar, String cardImageURL, ConsumerTypeApiEnum userType, Long genderId, String phone, Long zipcodeStateId, Long zipcodeId) {
        super(email, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatar = avatar;
        this.cardImageURL = cardImageURL;
        this.userType = userType;
        this.genderId = genderId;
        this.phone = phone;
        this.zipcodeStateId = zipcodeStateId;
        this.zipcodeId = zipcodeId;
    }
}
