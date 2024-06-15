package io.oigres.ecomm.service.users.jobs;

import org.springframework.stereotype.Component;

@Component
public class CardImagesCleanUpJobConfiguration {
    private int initialDelay;
    private int fixedRate;

    public CardImagesCleanUpJobConfiguration() {
        this.initialDelay = (int) Math.round(Math.random() * 10) + 5; // random between 5 and 15;
        this.fixedRate = (int) Math.round(Math.random() * 10) + 55; // random between 55 and 65
    }

    public int getInitialDelay() {
        return initialDelay;
    }

    public int getFixedRate() {
        return fixedRate;
    }
}
