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

import io.oigres.ecomm.service.users.api.model.CreateUserRequest;
import io.oigres.ecomm.service.users.api.model.enums.ConsumerTypeApiEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class CreateConsumerUserRequest extends CreateUserRequest {
  @Schema(name = "firstName", example = "", required = true)
  @NotEmpty
  private String firstName;

  @Schema(name = "lastName", example = "", required = true)
  @NotEmpty
  private String lastName;

  @Schema(name = "avatar", example = "", required = false)
  private String avatar;

  @Schema(name = "cardImageURL", example = "", required = false)
  @NotNull private String cardImageURL;

  @Schema(name = "userType", example = "RECREATIONAL", required = true)
  @NotNull private ConsumerTypeApiEnum userType;

  @Schema(name = "genderId", example = "", required = true)
  @NotNull private Long genderId;

  @Schema(name = "phone", example = "", required = true)
  @NotEmpty
  private String phone;

  @Schema(name = "zipcodeStateId", example = "", required = true)
  @NotNull private Long zipcodeStateId;

  @Schema(name = "zipcodeId", example = "", required = true)
  @NotNull private Long zipcodeId;
}
