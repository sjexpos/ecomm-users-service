package io.oigres.ecomm.service.users.jobs;

import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class MessageRelayServiceJobConfiguration {
    private int processInitialDelay;
    private int processFixedRate;
    private int cleanUpInitialDelay;
    private int cleanUpFixedRate;

    public MessageRelayServiceJobConfiguration() {
        this.processInitialDelay = (int) Math.round(Math.random() * 3) + 3; // random between 3 and 6;
        this.processFixedRate = (int) Math.round(Math.random() * 3) + 3; // random between 3 and 6;
        this.cleanUpInitialDelay = (int) Math.round(Math.random() * 3) + 3; // random between 3 and 6;
        this.cleanUpFixedRate = (int) Math.round(Math.random() * 3) + 3; // random between 3 and 6;
    }

}
