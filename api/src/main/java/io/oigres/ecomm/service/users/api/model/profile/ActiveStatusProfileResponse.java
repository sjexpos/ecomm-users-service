package io.oigres.ecomm.service.users.api.model.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ActiveStatusProfileResponse implements Serializable {
    private Long id;
    private Boolean enabled;
}
