package io.oigres.ecomm.service.users.api.model.dispensary;

import io.oigres.ecomm.service.users.api.model.CreateUserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateDispensaryUserResponse extends CreateUserResponse {
    private Long dispensaryId;

    private Boolean isActive;

    @Builder(builderMethodName = "dispensaryResponseBuilder")
    public CreateDispensaryUserResponse(Long id, String email, Long dispensaryId, Boolean isActive) {
        super(id, email);
        this.dispensaryId = dispensaryId;
        this.isActive = isActive;
    }
}
