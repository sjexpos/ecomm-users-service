package io.oigres.ecomm.service.users.usecases.users.images;

import org.springframework.stereotype.Component;

import io.oigres.ecomm.service.users.domain.Card;
import io.oigres.ecomm.service.users.domain.CardImage;
import io.oigres.ecomm.service.users.enums.ResourceStatusEnum;
import io.oigres.ecomm.service.users.repository.profiles.CardImageRepository;
import io.oigres.ecomm.service.users.repository.profiles.CardRepository;
import io.oigres.ecomm.service.users.repository.profiles.ConsumerProfileRepository;

import java.time.LocalDateTime;

@Component
public class ChangeCardImageStatusUseCaseImpl implements ChangeCardImageStatusUseCase {
    private final CardImageRepository cardImageRepository;
    private final CardRepository cardRepository;
    private final ConsumerProfileRepository consumerProfileRepository;

    public ChangeCardImageStatusUseCaseImpl(CardImageRepository cardImageRepository,
                                            ConsumerProfileRepository consumerProfileRepository,
                                            CardRepository cardRepository) {
        this.cardImageRepository = cardImageRepository;
        this.consumerProfileRepository = consumerProfileRepository;
        this.cardRepository = cardRepository;
    }

    @Override
    public CardImage handle(String imageUrl) {
        return cardImageRepository.findByImageURL(imageUrl).map(i -> {
                    if (!ResourceStatusEnum.UPLOADED.equals(i.getStatus())) {
                        i.setStatus(ResourceStatusEnum.UPLOADED);
                        markUserAsActive(imageUrl);
                        i = cardImageRepository.save(i);
                    }
                    return i;
                }
        ).orElseGet(() -> {
            CardImage cardImage = CardImage.builder().status(ResourceStatusEnum.PENDING).imageURL(imageUrl).build();
            CardImage savedCardImage = cardImageRepository.save(cardImage);
            Card card = Card.builder().mmjCard(true).idCard(true).createdAt(LocalDateTime.now()).cardImage(savedCardImage).build();
            cardRepository.save(card);
            return savedCardImage;
        });
    }

    private void markUserAsActive(String imageUrl) {
        consumerProfileRepository.findByCardImageUrl(imageUrl).ifPresent(c -> {
            if (!c.getEnabled()) {
                c.setEnabled(true);
                consumerProfileRepository.save(c);
            }
        });
    }
}
