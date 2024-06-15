package io.oigres.ecomm.service.users.api.model.consumer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetConsumerStateTax implements Serializable {
    private BigDecimal tax;
}
