package io.oigres.ecomm.service.users.usecases.users.images;

import io.oigres.ecomm.service.users.usecases.users.images.model.UpdateProfileImageDTO;

public interface ChangeProfileImageStatusUseCase {
     UpdateProfileImageDTO handle(String imageUrl);
}
