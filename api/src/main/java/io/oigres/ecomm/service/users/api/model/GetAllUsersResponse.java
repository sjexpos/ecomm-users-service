package io.oigres.ecomm.service.users.api.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetAllUsersResponse implements Serializable {
    private Long userId;
    private String email;

}
