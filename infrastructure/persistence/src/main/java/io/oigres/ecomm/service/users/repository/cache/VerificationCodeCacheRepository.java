/**********
 This project is free software; you can redistribute it and/or modify it under
 the terms of the GNU General Public License as published by the
 Free Software Foundation; either version 3.0 of the License, or (at your
 option) any later version. (See <https://www.gnu.org/licenses/gpl-3.0.html>.)

 This project is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 more details.

 You should have received a copy of the GNU General Public License
 along with this project; if not, write to the Free Software Foundation, Inc.,
 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 **********/
// Copyright (c) 2024-2025 Sergio Exposito.  All rights reserved.              

package io.oigres.ecomm.service.users.repository.cache;

import io.oigres.ecomm.service.users.domain.cache.VerificationCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

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
    log.debug(
        "Storing verification code in cache {} for email {}",
        CacheNames.VERIFICATION_CODES_CACHE_NAME,
        verificationCode.getEmail());
    return verificationCode;
  }
}
