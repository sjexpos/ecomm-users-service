package io.oigres.ecomm.service.users.api.model.dispensary;

import io.oigres.ecomm.service.users.api.model.UpdateProfileRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
public class UpdateDispensaryProfileRequest extends UpdateProfileRequest {
    @Schema(name = "dispensaryId", example = "", required = true)
    @NotNull
    private Long dispensaryId;
}
