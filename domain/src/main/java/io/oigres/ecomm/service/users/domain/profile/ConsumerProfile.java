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

import io.oigres.ecomm.service.users.domain.*;
import io.oigres.ecomm.service.users.enums.ConsumerTypeEnum;
import io.oigres.ecomm.service.users.enums.ResourceStatusEnum;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@DiscriminatorValue("2")
public class ConsumerProfile extends Profile {
  private static final long serialVersionUID = -4164258600038645847L;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "profile_image_id")
  private ProfileImage profileImage;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "gender_id")
  private Gender gender;

  @Column(name = "phone")
  private String phone;

  @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "card_id")
  private Card card;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "zip_code_id")
  private ZipCode zipCode;

  @Column(name = "verified")
  private Boolean verified;

  @Builder
  public ConsumerProfile(
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
      String firstName,
      String lastName,
      ProfileImage profileImage,
      Gender gender,
      String phone,
      Card card,
      ZipCode zipCode,
      Boolean verified) {
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
    this.firstName = firstName;
    this.lastName = lastName;
    this.profileImage = profileImage;
    this.gender = gender;
    this.phone = phone;
    this.card = card;
    this.zipCode = zipCode;
    this.verified = verified;
  }

  public ConsumerTypeEnum getUserType() {
    if (card != null) {
      if (card.getMmjCard()) {
        return ConsumerTypeEnum.MEDICAL;
      } else if (card.getIdCard()) {
        return ConsumerTypeEnum.RECREATIONAL;
      }
    }
    return null;
  }

  public void setUserType(ConsumerTypeEnum userType) {
    if (userType != null) {
      if (card == null) card = new Card();
      if (userType.equals(ConsumerTypeEnum.MEDICAL)) {
        card.setMmjCard(Boolean.TRUE);
        card.setIdCard(Boolean.FALSE);
      } else if (userType.equals(ConsumerTypeEnum.RECREATIONAL)) {
        card.setMmjCard(Boolean.FALSE);
        card.setIdCard(Boolean.TRUE);
      }
    }
  }

  public String getMmjCardImage() {
    String response = null;
    if (card != null) {
      response =
          card.getMmjCard()
              ? ((card.getCardImage() != null
                      && card.getCardImage().getStatus().equals(ResourceStatusEnum.UPLOADED))
                  ? card.getCardImage().getImageURL()
                  : null)
              : null;
    }
    return response;
  }
}
