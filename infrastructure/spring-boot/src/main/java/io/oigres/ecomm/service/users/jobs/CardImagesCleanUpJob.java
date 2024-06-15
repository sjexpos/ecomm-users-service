package io.oigres.ecomm.service.users.jobs;

import io.oigres.ecomm.service.users.config.UsersAssetsCleanUpConfig;
import io.oigres.ecomm.service.users.usecases.users.images.CleanUpBrokenCardImagesUseCase;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class CardImagesCleanUpJob {
    static final int PAGE_NUMBER = 0;
    static final int PAGE_SIZE_MAX = 101;
    static final int PAGE_SIZE_MIN = 100;
    private Duration imagesTimeAfterOffset;
    private final CleanUpBrokenCardImagesUseCase cleanUpBrokenCardImagesUseCase;

    public CardImagesCleanUpJob(CleanUpBrokenCardImagesUseCase cleanUpBrokenCardImagesUseCase,
                                UsersAssetsCleanUpConfig usersAssetsCleanUpConfig) {
        this.cleanUpBrokenCardImagesUseCase = cleanUpBrokenCardImagesUseCase;
        this.imagesTimeAfterOffset = usersAssetsCleanUpConfig.getCards().getImagesTimeAfterOffset();
    }

    @Async
    @Scheduled(initialDelayString = "#{@cardImagesCleanUpJobConfiguration.getInitialDelay()}",
            fixedDelayString = "#{@cardImagesCleanUpJobConfiguration.getFixedRate()}",
            timeUnit = TimeUnit.MINUTES
    )
    public void cleanUpBrokenCardImages() {
        Random random = new Random();
        int randomPageSize = random.nextInt(PAGE_SIZE_MAX - PAGE_SIZE_MIN) + PAGE_SIZE_MIN;
        Sort.Direction randomDirection = randomPageSize % 2 == 0 ? Sort.Direction.ASC : Sort.Direction.DESC;
        PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, randomPageSize, randomDirection, "id");

        log.info("Cleaning up Cards' broken assets. (random page size: {}, random page direction: {})", randomPageSize, randomDirection);
        cleanUpBrokenCardImagesUseCase.handle(pageRequest, imagesTimeAfterOffset);
    }
}
