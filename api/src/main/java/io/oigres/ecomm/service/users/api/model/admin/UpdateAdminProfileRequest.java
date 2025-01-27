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

import io.oigres.ecomm.service.users.api.model.UpdateProfileRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class UpdateAdminProfileRequest extends UpdateProfileRequest {
  @Schema(name = "firstName", example = "SomeName", required = true)
  @NotEmpty
  private String firstName;

  @Schema(name = "lastName", example = "SomeLastName", required = true)
  @NotEmpty
  private String lastName;

  @Schema(name = "img", example = "The image URL", required = false)
  private String img;

  @Schema(name = "phone", example = "Phone number. Just the numbers", required = true)
  @NotEmpty
  private String phone;

  @Schema(name = "email", example = "The email.", required = true)
  @NotEmpty
  @Email
  private String email;
}
