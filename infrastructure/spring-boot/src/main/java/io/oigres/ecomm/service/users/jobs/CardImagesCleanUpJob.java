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

package io.oigres.ecomm.service.users.jobs;

import io.oigres.ecomm.service.users.config.UsersAssetsCleanUpConfig;
import io.oigres.ecomm.service.users.usecases.users.images.CleanUpBrokenCardImagesUseCase;
import java.time.Duration;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CardImagesCleanUpJob {
  static final int PAGE_NUMBER = 0;
  static final int PAGE_SIZE_MAX = 101;
  static final int PAGE_SIZE_MIN = 100;
  private static final Random RANDOM = new Random();

  private Duration imagesTimeAfterOffset;
  private final CleanUpBrokenCardImagesUseCase cleanUpBrokenCardImagesUseCase;

  public CardImagesCleanUpJob(
      CleanUpBrokenCardImagesUseCase cleanUpBrokenCardImagesUseCase,
      UsersAssetsCleanUpConfig usersAssetsCleanUpConfig) {
    this.cleanUpBrokenCardImagesUseCase = cleanUpBrokenCardImagesUseCase;
    this.imagesTimeAfterOffset = usersAssetsCleanUpConfig.getCards().getImagesTimeAfterOffset();
  }

  @Async
  @Scheduled(
      initialDelayString = "#{@cardImagesCleanUpJobConfiguration.getInitialDelay()}",
      fixedDelayString = "#{@cardImagesCleanUpJobConfiguration.getFixedRate()}",
      timeUnit = TimeUnit.MINUTES)
  public void cleanUpBrokenCardImages() {
    int randomPageSize = RANDOM.nextInt(PAGE_SIZE_MAX - PAGE_SIZE_MIN) + PAGE_SIZE_MIN;
    Sort.Direction randomDirection =
        randomPageSize % 2 == 0 ? Sort.Direction.ASC : Sort.Direction.DESC;
    PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, randomPageSize, randomDirection, "id");

    log.info(
        "Cleaning up Cards' broken assets. (random page size: {}, random page direction: {})",
        randomPageSize,
        randomDirection);
    cleanUpBrokenCardImagesUseCase.handle(pageRequest, imagesTimeAfterOffset);
  }
}
