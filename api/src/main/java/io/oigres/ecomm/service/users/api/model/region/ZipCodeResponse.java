package io.oigres.ecomm.service.users.api.model.region;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ZipCodeResponse implements Serializable {
    private Long id;
    private Integer code;
    private String city;
    private StateResponse state;
    private BigDecimal tax;
}
