package io.oigres.ecomm.service.users.usecases.users.images;

import org.springframework.data.domain.Pageable;

import java.time.Duration;

public interface CleanUpBrokenCardImagesUseCase {
    void handle(Pageable pageable, Duration timeOffset);
}
