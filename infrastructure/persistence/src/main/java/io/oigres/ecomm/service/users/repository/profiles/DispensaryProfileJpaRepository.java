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

import io.oigres.ecomm.service.users.domain.profile.DispensaryProfile;
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
public interface DispensaryProfileJpaRepository
    extends SearchRepository<DispensaryProfile, Long>, DispensaryProfileRepository {
  @Override
  @Query("SELECT d FROM DispensaryProfile d WHERE (d.user.id = :userId) AND (d.deletedAt is null)")
  Optional<DispensaryProfile> findById(@Param("userId") Long userId);

  @Override
  @Query(
      "SELECT d FROM DispensaryProfile d WHERE (d.dispensaryId = :dispensaryId) AND (d.deletedAt is"
          + " null)")
  Optional<DispensaryProfile> findByDispensaryId(@Param("dispensaryId") Long dispensaryId);

  @Override
  @Query("SELECT d FROM DispensaryProfile d WHERE (d.deletedAt is null)")
  Page<DispensaryProfile> findAll(Pageable pageable);
}
