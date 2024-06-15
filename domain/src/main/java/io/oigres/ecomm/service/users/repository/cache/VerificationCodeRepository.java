package io.oigres.ecomm.service.users.repository.cache;

import io.oigres.ecomm.service.users.domain.cache.VerificationCode;

public interface VerificationCodeRepository {
    VerificationCode findByEmail(String email);

    VerificationCode save(VerificationCode verificationCode);
}
