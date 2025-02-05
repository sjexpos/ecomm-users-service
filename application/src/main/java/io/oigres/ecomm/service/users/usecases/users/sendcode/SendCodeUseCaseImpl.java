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

package io.oigres.ecomm.service.users.usecases.users.sendcode;

import io.oigres.ecomm.service.users.domain.cache.VerificationCode;
import io.oigres.ecomm.service.users.domain.profile.ConsumerProfile;
import io.oigres.ecomm.service.users.exception.InvalidException;
import io.oigres.ecomm.service.users.exception.UserNotFoundException;
import io.oigres.ecomm.service.users.repository.cache.VerificationCodeRepository;
import io.oigres.ecomm.service.users.repository.profiles.ConsumerProfileRepository;
import java.time.Instant;
import java.util.Optional;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SendCodeUseCaseImpl implements SendCodeUseCase {
  private final ConsumerProfileRepository consumerProfileRepository;
  private final VerificationCodeRepository verificationCodeRepository;
  private final Random random = new Random();

  public SendCodeUseCaseImpl(
      ConsumerProfileRepository consumerProfileRepository,
      VerificationCodeRepository verificationCodeRepository) {
    this.consumerProfileRepository = consumerProfileRepository;
    this.verificationCodeRepository = verificationCodeRepository;
  }

  @Override
  public void handle(String email) throws UserNotFoundException, InvalidException {
    Optional<ConsumerProfile> opConsumerProfile = consumerProfileRepository.findByUserEmail(email);
    if (opConsumerProfile.isEmpty()
        || opConsumerProfile.get().getUser().isDeleted()
        || Boolean.FALSE.equals(opConsumerProfile.get().getEnabled())) {
      throw new UserNotFoundException();
    }
    ConsumerProfile consumerProfile = opConsumerProfile.get();
    if (Boolean.TRUE.equals(consumerProfile.getVerified())) {
      throw new InvalidException(String.format("User %s is already verified", email));
    }
    String code = String.format("%04d", random.nextInt(10000));
    verificationCodeRepository.save(
        VerificationCode.builder().email(email).code(code).createdAt(Instant.now()).build());
    String consumerPhone = consumerProfile.getPhone();
    sendSMS(consumerPhone, code);
  }

  private void sendSMS(String phone, String code) {
    log.info("TEMPORAL LOG - Sending to {} the verification code {}", phone, code);
  }
}
