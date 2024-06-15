package io.oigres.ecomm.service.users.domain.cache;

import lombok.*;

import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VerificationCode {
    private String email;
    private String code;
    private Instant createdAt;
}
