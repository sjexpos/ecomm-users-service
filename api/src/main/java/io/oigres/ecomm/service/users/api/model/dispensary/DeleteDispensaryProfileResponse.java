package io.oigres.ecomm.service.users.api.model.dispensary;

import io.oigres.ecomm.service.users.api.model.DeleteProfileResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
public class DeleteDispensaryProfileResponse extends DeleteProfileResponse {
    private Long dispensaryId;

    @Builder(builderMethodName = "deleteDispensaryResponseBuilder")
    public DeleteDispensaryProfileResponse(Long id, String email, Boolean isActive, Long dispensaryId) {
        super(id, email, isActive);
        this.dispensaryId = dispensaryId;
    }
}
