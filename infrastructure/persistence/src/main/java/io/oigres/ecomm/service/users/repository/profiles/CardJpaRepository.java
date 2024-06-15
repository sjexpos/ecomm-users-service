package io.oigres.ecomm.service.users.repository.profiles;

import io.oigres.ecomm.service.users.domain.Card;
import io.oigres.ecomm.service.users.repository.SearchRepository;
import io.oigres.ecomm.service.users.repository.profiles.CardRepository;

public interface CardJpaRepository extends SearchRepository<Card, Long>, CardRepository {
}
