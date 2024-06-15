package io.oigres.ecomm.service.users.usecases.users.images;

import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.oigres.ecomm.service.users.domain.Card;
import io.oigres.ecomm.service.users.domain.CardImage;
import io.oigres.ecomm.service.users.enums.ResourceStatusEnum;
import io.oigres.ecomm.service.users.repository.BlobRepository;
import io.oigres.ecomm.service.users.repository.profiles.CardImageRepository;
import io.oigres.ecomm.service.users.repository.profiles.CardRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CleanUpBrokenCardImagesUseCaseImpl implements CleanUpBrokenCardImagesUseCase {
    private final CardImageRepository cardImageRepository;
    private final CardRepository cardRepository;
    private final BlobRepository blobRepository;

    public CleanUpBrokenCardImagesUseCaseImpl(CardImageRepository cardImageRepository, CardRepository cardRepository, BlobRepository blobRepository) {
        this.cardImageRepository = cardImageRepository;
        this.cardRepository = cardRepository;
        this.blobRepository = blobRepository;
    }

    @Override
    @Transactional
    public void handle(Pageable pageable, Duration timeOffset) {
        List<ResourceStatusEnum> statuses = Arrays.asList(ResourceStatusEnum.PENDING, ResourceStatusEnum.DELETED);
        Page<CardImage> elegibleForDeletionAssets = cardImageRepository.findElegibleForDeletion(pageable, statuses);
        List<CardImage> selectedForDeletionAssets = elegibleForDeletionAssets.getContent().stream()
                .filter(cardImage -> (cardImage.getModifiedAt() != null || cardImage.getCreatedAt() != null)
                                && LocalDateTime.now().isAfter(
                                (cardImage.getModifiedAt() != null ? cardImage.getModifiedAt() : cardImage.getCreatedAt()).plus(timeOffset)
                        )
                )
                .collect(Collectors.toList());
        processAssetsMarkedAsDeleted(selectedForDeletionAssets);
        deleteAssetsFromDatabase(selectedForDeletionAssets);
    }

    private void processAssetsMarkedAsDeleted(List<CardImage> selectedForDeletionAssets) {
        List<CardImage> selectedForDeletionAssetsWithDeletedStatus = selectedForDeletionAssets.stream()
                .filter(cardImage -> ResourceStatusEnum.DELETED.equals(cardImage.getStatus()))
                .collect(Collectors.toList());

        if (!selectedForDeletionAssetsWithDeletedStatus.isEmpty()) {
            List<String> keysToDeleteFromS3 = selectedForDeletionAssetsWithDeletedStatus.stream()
                    .map(CardImage::getImageURL)
                    .collect(Collectors.toList());

            List<String> deletedKeys = blobRepository.deleteAllByKey(keysToDeleteFromS3);

            if (!deletedKeys.isEmpty() && deletedKeys.size() < keysToDeleteFromS3.size()) {
                List<CardImage> notDeletedFromS3 = Collections.emptyList();
                selectedForDeletionAssetsWithDeletedStatus
                        .forEach(cardImage ->
                                {
                                    if (!deletedKeys.contains(cardImage.getImageURL())) {
                                        notDeletedFromS3.add(cardImage);
                                    }
                                }
                        );
                if (!notDeletedFromS3.isEmpty()) {
                    selectedForDeletionAssets.removeAll(notDeletedFromS3);
                }
            }
        }
    }

    private void deleteAssetsFromDatabase(List<CardImage> selectedForDeletionAssets) {
        if (selectedForDeletionAssets != null && !selectedForDeletionAssets.isEmpty()) {
            List<Long> selectedForDeletionAssetsIds = selectedForDeletionAssets.stream()
                    .map(CardImage::getId)
                    .collect(Collectors.toList());
            if (!selectedForDeletionAssetsIds.isEmpty()) {
                log.info("cleaned up ids list: " + selectedForDeletionAssetsIds);
                selectedForDeletionAssets.stream()
                        .forEach(cardImage -> {
                                    Optional<Card> cardOptional = cardRepository.findByCardImage_imageURL(cardImage.getImageURL());
                                    if (cardOptional.isPresent() && !cardOptional.isEmpty()) {
                                        cardOptional.get().setCardImage(null);
                                        cardRepository.save(cardOptional.get());
                                    }
                                }
                        );
                cardImageRepository.deleteByIdIn(selectedForDeletionAssetsIds);
            }
        }
    }
}
