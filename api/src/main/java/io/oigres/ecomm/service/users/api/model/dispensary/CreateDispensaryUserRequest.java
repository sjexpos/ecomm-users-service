package io.oigres.ecomm.service.users.api.model.dispensary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import io.oigres.ecomm.service.users.api.model.CreateUserRequest;



@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper=true)
public class CreateDispensaryUserRequest extends CreateUserRequest {

    @NotNull
    private Long dispensaryId;

    @Builder(builderMethodName = "dispensaryBuilder")
    public CreateDispensaryUserRequest(@NotEmpty @Email String email, @NotEmpty String password, Long dispensaryId) {
        super(email, password);
        this.dispensaryId = dispensaryId;
    }
}
