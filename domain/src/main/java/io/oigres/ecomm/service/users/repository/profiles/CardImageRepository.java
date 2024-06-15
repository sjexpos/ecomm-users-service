package io.oigres.ecomm.service.users.repository.profiles;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.oigres.ecomm.service.users.domain.CardImage;
import io.oigres.ecomm.service.users.enums.ResourceStatusEnum;

import java.util.List;
import java.util.Optional;

public interface CardImageRepository {
    Optional<CardImage> findByImageURL(String imageUrl);
    Page<CardImage> findElegibleForDeletion(Pageable pageable, List<ResourceStatusEnum> statuses);
    CardImage save(CardImage cardImage);
    void deleteByIdIn(List<Long> ids);
}
