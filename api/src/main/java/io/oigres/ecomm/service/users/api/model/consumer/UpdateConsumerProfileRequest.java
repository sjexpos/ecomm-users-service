package io.oigres.ecomm.service.users.api.model.consumer;

import io.oigres.ecomm.service.users.api.model.UpdateProfileRequest;
import io.oigres.ecomm.service.users.api.model.enums.ConsumerTypeApiEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateConsumerProfileRequest extends UpdateProfileRequest {
    @Schema(name = "firstName", example = "", required = true)
    @NotEmpty
    private String firstName;
    @Schema(name = "lastName", example = "", required = true)
    @NotEmpty
    private String lastName;
    @Schema(name = "cardImageURL", example = "", required = true)
    private String cardImageURL;
    @Schema(name = "avatar", example = "")
    private String avatar;
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
}
