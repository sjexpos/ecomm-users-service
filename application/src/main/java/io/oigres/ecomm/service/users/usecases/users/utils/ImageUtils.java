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

package io.oigres.ecomm.service.users.usecases.users.utils;

import io.oigres.ecomm.service.users.domain.ProfileImage;
import io.oigres.ecomm.service.users.domain.profile.AdminProfile;
import io.oigres.ecomm.service.users.domain.profile.ConsumerProfile;
import io.oigres.ecomm.service.users.enums.ResourceStatusEnum;

public class ImageUtils {

  private ImageUtils() {}

  public static String getProfileImageURLForConsumerUser(ConsumerProfile profile) {
    ProfileImage profileImage = profile.getProfileImage();
    return getProfileImageURL(profileImage);
  }

  public static String getProfileImageURLForAdminUser(AdminProfile profile) {
    ProfileImage profileImage = profile.getProfileImage();
    return getProfileImageURL(profileImage);
  }

  private static String getProfileImageURL(ProfileImage profileImage) {
    String url = null;
    if (profileImage != null
        && profileImage.getImageURL() != null
        && ResourceStatusEnum.UPLOADED.equals(profileImage.getStatus())) {
      url = profileImage.getImageURL();
    }
    return url;
  }
}
