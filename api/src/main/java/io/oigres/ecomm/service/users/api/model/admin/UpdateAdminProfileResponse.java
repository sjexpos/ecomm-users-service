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

package io.oigres.ecomm.service.users.api.model.admin;

import io.oigres.ecomm.service.users.api.model.UpdateProfileResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class UpdateAdminProfileResponse extends UpdateProfileResponse {
  private String firstName;
  private String lastName;
  private String img;
  private String phone;

  @Builder(builderMethodName = "updateAdminResponseBuilder")
  public UpdateAdminProfileResponse(
      Long id,
      String email,
      Boolean isActive,
      String firstName,
      String lastName,
      String img,
      String phone) {
    super(id, email, isActive);
    this.firstName = firstName;
    this.lastName = lastName;
    this.img = img;
    this.phone = phone;
  }
}
