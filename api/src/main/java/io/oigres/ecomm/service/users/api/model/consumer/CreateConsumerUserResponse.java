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

import io.oigres.ecomm.service.users.api.model.CreateUserResponse;
import io.oigres.ecomm.service.users.api.model.enums.ConsumerTypeApiEnum;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class CreateConsumerUserResponse extends CreateUserResponse {

  private String firstName;
  private String lastName;
  private String avatar;
  private Long genderId;
  private String phone;
  private String cardImageURL;
  private Long zipcodeStateId;
  private Long zipcodeId;
  private ConsumerTypeApiEnum userType;
  private Boolean isActive;
  private Boolean verified;
}
