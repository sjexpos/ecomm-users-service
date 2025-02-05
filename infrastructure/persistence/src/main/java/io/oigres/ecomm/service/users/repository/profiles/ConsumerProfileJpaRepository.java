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

import io.oigres.ecomm.service.users.domain.profile.ConsumerProfile;
import io.oigres.ecomm.service.users.repository.SearchRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ConsumerProfileJpaRepository
    extends SearchRepository<ConsumerProfile, Long>, ConsumerProfileRepository {
  @Override
  @Query("SELECT c FROM ConsumerProfile c WHERE (c.user.id = :userId) AND (c.deletedAt is null)")
  Optional<ConsumerProfile> findById(@Param("userId") Long userId);

  @Override
  @Query("SELECT c FROM ConsumerProfile c WHERE (c.deletedAt is null)")
  Page<ConsumerProfile> findAll(Pageable pageable);

  @Override
  @Query(
      "SELECT c FROM ConsumerProfile c WHERE (c.deletedAt is null) AND (c.card.cardImage.imageURL ="
          + " :cardImageUrl)")
  Optional<ConsumerProfile> findByCardImageUrl(@Param("cardImageUrl") String cardImageUrl);
}
