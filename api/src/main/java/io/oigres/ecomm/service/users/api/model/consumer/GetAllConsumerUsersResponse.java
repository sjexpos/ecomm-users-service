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

package io.oigres.ecomm.service.users.api.model.consumer;

import io.oigres.ecomm.service.users.api.model.GetAllUsersResponse;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetAllConsumerUsersResponse extends GetAllUsersResponse {
  private String firstName;
  private String lastName;
  private String avatar;
  private String gender;
  private String phone;
  private String state;
  private String zipCode;
  private String userType;
  private Boolean isActive;
  private Boolean verified;

  @Builder(builderMethodName = "getAllConsumerResponseBuilder")
  public GetAllConsumerUsersResponse(
      Long userId,
      String email,
      String firstName,
      String lastName,
      String avatar,
      String gender,
      String phone,
      String state,
      String zipCode,
      String userType,
      Boolean isActive,
      Boolean verified) {
    super(userId, email);
    this.firstName = firstName;
    this.lastName = lastName;
    this.avatar = avatar;
    this.gender = gender;
    this.phone = phone;
    this.state = state;
    this.zipCode = zipCode;
    this.userType = userType;
    this.isActive = isActive;
    this.verified = verified;
  }
}
