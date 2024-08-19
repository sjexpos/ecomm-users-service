package io.oigres.ecomm.service.users.api.model.dispensary;

import io.oigres.ecomm.service.users.api.model.UpdateProfileResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
public class UpdateDispensaryProfileResponse extends UpdateProfileResponse {
    private Long dispensaryId;

    @Builder(builderMethodName = "updateDispensaryResponseBuilder")
    public UpdateDispensaryProfileResponse(Long id, String email, Boolean isActive, Long dispensaryId) {
        super(id, email, isActive);
        this.dispensaryId = dispensaryId;
    }
}
