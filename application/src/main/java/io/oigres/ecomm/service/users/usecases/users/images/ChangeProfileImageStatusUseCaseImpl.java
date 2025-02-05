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

import io.oigres.ecomm.service.users.domain.ProfileImage;
import io.oigres.ecomm.service.users.enums.ResourceStatusEnum;
import io.oigres.ecomm.service.users.repository.ProfileImageRepository;
import io.oigres.ecomm.service.users.usecases.users.images.model.UpdateProfileImageDTO;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class ChangeProfileImageStatusUseCaseImpl implements ChangeProfileImageStatusUseCase {
  private final ProfileImageRepository profileImageRepository;

  public ChangeProfileImageStatusUseCaseImpl(ProfileImageRepository profileImageRepository) {
    this.profileImageRepository = profileImageRepository;
  }

  @Override
  public UpdateProfileImageDTO handle(String imageUrl) {
    ProfileImage profileImage;

    Optional<ProfileImage> optional = profileImageRepository.findByImageURL(imageUrl);

    if (optional.isPresent()) {
      profileImage = optional.get();
      profileImage.setStatus(ResourceStatusEnum.UPLOADED);
    } else {
      profileImage =
          ProfileImage.builder().status(ResourceStatusEnum.UPLOADED).imageURL(imageUrl).build();
    }

    profileImageRepository.save(profileImage);

    return UpdateProfileImageDTO.builder()
        .id(profileImage.getId())
        .imageURL(profileImage.getImageURL())
        .status(profileImage.getStatus().name())
        .build();
  }
}
