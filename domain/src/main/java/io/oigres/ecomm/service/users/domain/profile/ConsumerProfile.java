package io.oigres.ecomm.service.users.domain.profile;

import lombok.*;

import jakarta.persistence.*;

import io.oigres.ecomm.service.users.domain.*;
import io.oigres.ecomm.service.users.enums.ConsumerTypeEnum;
import io.oigres.ecomm.service.users.enums.ResourceStatusEnum;

import java.time.LocalDateTime;

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
    public ConsumerProfile(Long id, User user, ProfileType profileType, Boolean enabled, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy,
                           LocalDateTime deletedAt, String deletedBy, String firstName, String lastName, ProfileImage profileImage, Gender gender, String phone, Card card, ZipCode zipCode,
                           Boolean verified) {
        super(id, user, profileType, enabled, createdAt, createdBy, modifiedAt, modifiedBy, deletedAt, deletedBy);
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImage = profileImage;
        this.gender = gender;
        this.phone = phone;
        this.card = card;
        this.zipCode = zipCode;
        this.verified = verified;
    }

    public ConsumerTypeEnum getUserType(){
        if (card != null) {
            if(card.getMmjCard()){
                return ConsumerTypeEnum.MEDICAL;
            } else if (card.getIdCard()) {
                return ConsumerTypeEnum.RECREATIONAL;
            }
        }
        return null;
    }

    public void setUserType(ConsumerTypeEnum userType){
        if (userType != null) {
            if (card == null) card = new Card();
            if(userType.equals(ConsumerTypeEnum.MEDICAL)){
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
            response = card.getMmjCard() ? ((card.getCardImage() != null && card.getCardImage().getStatus().equals(ResourceStatusEnum.UPLOADED)) ? card.getCardImage().getImageURL() : null) : null;
        }
        return response;
    }

}
