package io.oigres.ecomm.service.users.repository.profiles;

import io.oigres.ecomm.service.users.domain.CardImage;
import io.oigres.ecomm.service.users.enums.ResourceStatusEnum;
import io.oigres.ecomm.service.users.repository.SearchRepository;
import io.oigres.ecomm.service.users.repository.profiles.CardImageRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardImageJpaRepository extends SearchRepository<CardImage, Long>, CardImageRepository {
    @Override
    @Query(value = "SELECT ci FROM Card c RIGHT JOIN c.cardImage ci WHERE ( (c IS NULL) OR ( (c IS NOT NULL) AND (ci.status IN (:statuses)) ) )")
    Page<CardImage> findElegibleForDeletion(Pageable pageable, @Param("statuses") List<ResourceStatusEnum> statuses);
}
