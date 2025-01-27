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

package io.oigres.ecomm.service.users.api;

public class ProfileErrorMessages {
  public static final String PROFILE_ERROR_MMJCARDIMAGE_USED =
      "Mmj card image is already used in other profile.";
  public static final String PROFILE_ERROR_AVATAR_IMAGE_USED =
      "Avatar image is already taken by other user.";
  public static final String PROFILE_ALREADY_EXIST = "Profile already exist";
  public static final String PROFILE_DELETED = "Profile was already deleted";
  public static final String USER_TYPE_NOT_EXIST = "User type not found";
  public static final String PROFILE_TYPE_NOT_FOUND = "Profile type not found";
  public static final String GENDER_NOT_EXIST = "Gender provided not exist";
  public static final String STATE_NOT_FOUND = "State provided not found";
  public static final String ZIPCODE_NOT_FOUND = "Zipcode provided not found";
  public static final String INVALID_PROFILE_IMAGE =
      "Profile image must be null or it must have a valid key";
  public static final String IMAGE_ALREADY_EXISTS = "Image is used by other user";
}
