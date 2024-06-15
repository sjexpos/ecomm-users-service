package io.oigres.ecomm.service.users.api.model.region;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StateResponse implements Serializable {
    private Long id;
    private String name;
    private String shortName;
}
