package io.oigres.ecomm.service.users.domain;

import lombok.*;

import java.net.URL;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StoreLocation {
    private URL uploadURL;
    private String httpMethod;
    private String key;
}
