package io.oigres.ecomm.service.users.api.model.dispensary;

import io.oigres.ecomm.service.users.api.model.UpdateProfileRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateDispensaryProfileRequest extends UpdateProfileRequest {
    @Schema(name = "dispensaryId", example = "", required = true)
    @NotNull
    private Long dispensaryId;
}
