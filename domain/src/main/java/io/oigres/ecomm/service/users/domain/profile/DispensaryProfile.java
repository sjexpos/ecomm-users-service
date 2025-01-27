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

package io.oigres.ecomm.service.users.domain.profile;

import io.oigres.ecomm.service.users.domain.Profile;
import io.oigres.ecomm.service.users.domain.ProfileType;
import io.oigres.ecomm.service.users.domain.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@DiscriminatorValue("3")
public class DispensaryProfile extends Profile {
  private static final long serialVersionUID = -4164258600038645847L;

  @Column(name = "dispensary_id")
  private Long dispensaryId;

  @Builder
  public DispensaryProfile(
      Long id,
      User user,
      ProfileType profileType,
      Boolean enabled,
      LocalDateTime createdAt,
      String createdBy,
      LocalDateTime modifiedAt,
      String modifiedBy,
      LocalDateTime deletedAt,
      String deletedBy,
      Long dispensaryId) {
    super(
        id,
        user,
        profileType,
        enabled,
        createdAt,
        createdBy,
        modifiedAt,
        modifiedBy,
        deletedAt,
        deletedBy);
    this.dispensaryId = dispensaryId;
  }
}
