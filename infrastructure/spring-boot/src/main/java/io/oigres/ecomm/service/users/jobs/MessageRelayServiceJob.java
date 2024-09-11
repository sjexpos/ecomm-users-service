package io.oigres.ecomm.service.users.jobs;

import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.oigres.ecomm.service.users.txoutbox.MessageRelayService;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MessageRelayServiceJob {
    private final MessageRelayService messageRelayService;

    public MessageRelayServiceJob(MessageRelayService messageRelayService) {
        this.messageRelayService = messageRelayService;
    }

    @Async
    @Scheduled(initialDelayString = "#{@messageRelayServiceJobConfiguration.getProcessInitialDelay()}",
            fixedDelayString = "#{messageRelayServiceJobConfiguration.getProcessFixedRate()}",
            timeUnit = TimeUnit.MINUTES
    )
    public void processOutboxes() {
        log.info("Running Message Relay Service for Transactional outbox");
        this.messageRelayService.processTransactionalOutboxes();
    }

    @Async
    @Scheduled(initialDelayString = "#{@messageRelayServiceJobConfiguration.getCleanUpInitialDelay()}",
            fixedDelayString = "#{messageRelayServiceJobConfiguration.getCleanUpFixedRate()}",
            timeUnit = TimeUnit.MINUTES
    )
    public void cleanDeliveredOutboxes() {
        log.info("Cleaning up Transactional outboxes");
        this.messageRelayService.cleanDeliveredTransactionalOutboxes();
    }

}