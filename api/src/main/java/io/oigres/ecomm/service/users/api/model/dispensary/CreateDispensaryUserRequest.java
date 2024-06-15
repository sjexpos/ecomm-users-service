package io.oigres.ecomm.service.users.api.model.dispensary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.oigres.ecomm.service.users.api.model.CreateUserRequest;



@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateDispensaryUserRequest extends CreateUserRequest {

    @NotNull
    private Long dispensaryId;

    @Builder(builderMethodName = "dispensaryBuilder")
    public CreateDispensaryUserRequest(@NotEmpty @Email String email, @NotEmpty String password, Long dispensaryId) {
        super(email, password);
        this.dispensaryId = dispensaryId;
    }
}
