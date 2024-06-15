package io.oigres.ecomm.service.users.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateProfileRequest implements Serializable {
    @Schema(name = "password", example = "", required = false)
    private String password;

}
