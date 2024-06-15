package io.oigres.ecomm.service.users.api.model.dispensary;

import io.oigres.ecomm.service.users.api.model.GetUserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetDispensaryUserResponse extends GetUserResponse {
    private Long dispensaryId;

    private Boolean isActive;

    @Builder(builderMethodName = "getDispensaryResponseBuilder")
    public GetDispensaryUserResponse(Long userId, String email, Long dispensaryId, Boolean isActive) {
        super(userId, email);
        this.dispensaryId = dispensaryId;
        this.isActive = isActive;
    }
}
