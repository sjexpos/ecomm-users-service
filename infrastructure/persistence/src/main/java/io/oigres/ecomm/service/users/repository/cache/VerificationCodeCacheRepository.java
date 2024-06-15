package io.oigres.ecomm.service.users.repository.cache;

import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import io.oigres.ecomm.service.users.domain.cache.VerificationCode;
import io.oigres.ecomm.service.users.repository.cache.VerificationCodeRepository;

@Component
@Slf4j
public class VerificationCodeCacheRepository implements VerificationCodeRepository {
    @Override
    @Cacheable(value = CacheNames.VERIFICATION_CODES_CACHE_NAME, key = "#email")
    public VerificationCode findByEmail(String email) {
        return null;
    }

    @Override
    @CachePut(value = CacheNames.VERIFICATION_CODES_CACHE_NAME, key = "#verificationCode.email")
    public VerificationCode save(VerificationCode verificationCode) {
        log.debug("Storing verification code in cache {} for email {}", CacheNames.VERIFICATION_CODES_CACHE_NAME, verificationCode.getEmail());
        return verificationCode;
    }
}
