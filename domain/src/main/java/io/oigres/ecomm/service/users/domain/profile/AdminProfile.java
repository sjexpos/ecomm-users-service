package io.oigres.ecomm.service.users.domain.profile;

import lombok.*;

import jakarta.persistence.*;

import io.oigres.ecomm.service.users.domain.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@DiscriminatorValue("1")
public class AdminProfile extends Profile {
    private static final long serialVersionUID = -4164258600038645847L;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_image_id")
    private ProfileImage profileImage;

    @Column(name = "phone")
    private String phone;

    @Builder
    public AdminProfile(Long id, User user, ProfileType profileType, Boolean enabled, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy, LocalDateTime deletedAt
            , String deletedBy, String firstName, String lastName, ProfileImage profileImage, String phone) {
        super(id, user, profileType, enabled, createdAt, createdBy, modifiedAt, modifiedBy, deletedAt, deletedBy);
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImage = profileImage;
        this.phone = phone;
    }
}
