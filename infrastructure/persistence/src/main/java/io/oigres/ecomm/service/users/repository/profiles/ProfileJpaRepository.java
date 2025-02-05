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

package io.oigres.ecomm.service.users.repository.profiles;

import io.oigres.ecomm.service.users.domain.Profile;
import io.oigres.ecomm.service.users.enums.ProfileTypeEnum;
import io.oigres.ecomm.service.users.repository.SearchRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ProfileJpaRepository extends SearchRepository<Profile, Long>, ProfileRepository {
  @Override
  @Query(
      "SELECT p FROM Profile p WHERE (p.user.id = :userId) AND (p.profileType.profile ="
          + " :profileType) AND (p.deletedAt is null)")
  Optional<Profile> findByIdAndProfileType(
      @Param("userId") Long userId, @Param("profileType") ProfileTypeEnum profileType);

  @Query(
      value =
          "SELECT p.* FROM profiles p LEFT JOIN profile_images pi ON (p.profile_image_id = pi.id)"
              + " WHERE p.id <> :profileId AND pi.image_url = :imageUrl ",
      nativeQuery = true)
  List<Profile> existsByNotIdAndProfileImage_ImageURL(
      @Param("profileId") Long id, @Param("imageUrl") String imageUrl);
}
