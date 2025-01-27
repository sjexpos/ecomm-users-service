/**********
 This project is free software; you can redistribute it and/or modify it under
 the terms of the GNU General Public License as published by the
 Free Software Foundation; either version 3.0 of the License, or (at your
 option) any later version. (See <https://www.gnu.org/licenses/gpl-3.0.html>.)

 This project is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 more details.

 You should have received a copy of the GNU General Public License
 along with this project; if not, write to the Free Software Foundation, Inc.,
 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 **********/
// Copyright (c) 2024-2025 Sergio Exposito.  All rights reserved.              

package io.oigres.ecomm.service.users.usecases.users.images;

import io.oigres.ecomm.service.users.domain.Card;
import io.oigres.ecomm.service.users.domain.CardImage;
import io.oigres.ecomm.service.users.enums.ResourceStatusEnum;
import io.oigres.ecomm.service.users.repository.profiles.CardImageRepository;
import io.oigres.ecomm.service.users.repository.profiles.CardRepository;
import io.oigres.ecomm.service.users.repository.profiles.ConsumerProfileRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class ChangeCardImageStatusUseCaseImpl implements ChangeCardImageStatusUseCase {
  private final CardImageRepository cardImageRepository;
  private final CardRepository cardRepository;
  private final ConsumerProfileRepository consumerProfileRepository;

  public ChangeCardImageStatusUseCaseImpl(
      CardImageRepository cardImageRepository,
      ConsumerProfileRepository consumerProfileRepository,
      CardRepository cardRepository) {
    this.cardImageRepository = cardImageRepository;
    this.consumerProfileRepository = consumerProfileRepository;
    this.cardRepository = cardRepository;
  }

  @Override
  public CardImage handle(String imageUrl) {
    return cardImageRepository
        .findByImageURL(imageUrl)
        .map(
            i -> {
              if (!ResourceStatusEnum.UPLOADED.equals(i.getStatus())) {
                i.setStatus(ResourceStatusEnum.UPLOADED);
                markUserAsActive(imageUrl);
                i = cardImageRepository.save(i);
              }
              return i;
            })
        .orElseGet(
            () -> {
              CardImage cardImage =
                  CardImage.builder().status(ResourceStatusEnum.PENDING).imageURL(imageUrl).build();
              CardImage savedCardImage = cardImageRepository.save(cardImage);
              Card card =
                  Card.builder()
                      .mmjCard(true)
                      .idCard(true)
                      .createdAt(LocalDateTime.now())
                      .cardImage(savedCardImage)
                      .build();
              cardRepository.save(card);
              return savedCardImage;
            });
  }

  private void markUserAsActive(String imageUrl) {
    consumerProfileRepository
        .findByCardImageUrl(imageUrl)
        .ifPresent(
            c -> {
              if (!c.getEnabled()) {
                c.setEnabled(true);
                consumerProfileRepository.save(c);
              }
            });
  }
}
