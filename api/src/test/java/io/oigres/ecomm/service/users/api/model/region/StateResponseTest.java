package io.oigres.ecomm.service.users.api.model.region;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StateResponseTest {

    @Test
    void check_get_and_set() {
        StateResponse response = StateResponse.builder()
                                    .build();       
        response.setId(15L);
        response.setName("Oregon");
        response.setShortName("OR");

        Assertions.assertEquals(15L, response.getId());
        Assertions.assertEquals("Oregon", response.getName());
        Assertions.assertEquals("OR", response.getShortName());
    }

}
