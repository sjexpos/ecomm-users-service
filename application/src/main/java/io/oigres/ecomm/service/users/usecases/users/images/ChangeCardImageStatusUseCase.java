package io.oigres.ecomm.service.users.usecases.users.images;

import io.oigres.ecomm.service.users.domain.CardImage;

public interface ChangeCardImageStatusUseCase {
    CardImage handle(String imageUrl);
}
