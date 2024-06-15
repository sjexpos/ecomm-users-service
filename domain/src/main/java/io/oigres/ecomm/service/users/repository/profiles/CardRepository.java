package io.oigres.ecomm.service.users.repository.profiles;

import java.util.Optional;

import io.oigres.ecomm.service.users.domain.Card;

public interface CardRepository {
    Optional<Card> findByCardImage_imageURL(String imageUrl);
    Card save(Card cardImage);
}
