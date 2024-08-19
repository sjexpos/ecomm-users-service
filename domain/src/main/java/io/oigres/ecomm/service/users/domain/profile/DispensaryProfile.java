package io.oigres.ecomm.service.users.domain.profile;

import lombok.*;

import jakarta.persistence.*;

import io.oigres.ecomm.service.users.domain.Profile;
import io.oigres.ecomm.service.users.domain.ProfileType;
import io.oigres.ecomm.service.users.domain.User;

import java.time.LocalDateTime;

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
    public DispensaryProfile(Long id, User user, ProfileType profileType, Boolean enabled, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy, LocalDateTime deletedAt, String deletedBy, Long dispensaryId) {
        super(id, user, profileType, enabled, createdAt, createdBy, modifiedAt, modifiedBy, deletedAt, deletedBy);
        this.dispensaryId = dispensaryId;
    }

}
