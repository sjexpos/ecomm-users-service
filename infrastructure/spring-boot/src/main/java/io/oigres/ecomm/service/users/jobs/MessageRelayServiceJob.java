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

import io.oigres.ecomm.service.users.txoutbox.MessageRelayService;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageRelayServiceJob {
  private final MessageRelayService messageRelayService;

  public MessageRelayServiceJob(MessageRelayService messageRelayService) {
    this.messageRelayService = messageRelayService;
  }

  @Async
  @Scheduled(
      initialDelayString = "#{@messageRelayServiceJobConfiguration.getProcessInitialDelay()}",
      fixedDelayString = "#{messageRelayServiceJobConfiguration.getProcessFixedRate()}",
      timeUnit = TimeUnit.MINUTES)
  public void processOutboxes() {
    log.info("Running Message Relay Service for Transactional outbox");
    this.messageRelayService.processTransactionalOutboxes();
  }

  @Async
  @Scheduled(
      initialDelayString = "#{@messageRelayServiceJobConfiguration.getCleanUpInitialDelay()}",
      fixedDelayString = "#{messageRelayServiceJobConfiguration.getCleanUpFixedRate()}",
      timeUnit = TimeUnit.MINUTES)
  public void cleanDeliveredOutboxes() {
    log.info("Cleaning up Transactional outboxes");
    this.messageRelayService.cleanDeliveredTransactionalOutboxes();
  }
}
