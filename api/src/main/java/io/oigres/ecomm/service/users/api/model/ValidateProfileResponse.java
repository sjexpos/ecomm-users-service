package io.oigres.ecomm.service.users.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ValidateProfileResponse implements Serializable {
    private Long profileId;
    private String profileType;
    private Boolean isEnabled;
    private Long dispensaryId;

    public ValidateProfileResponse(Long profileId, String profileType, Boolean isEnabled) {
        this.profileId = profileId;
        this.profileType = profileType;
        this.isEnabled = isEnabled;
    }
    
}
