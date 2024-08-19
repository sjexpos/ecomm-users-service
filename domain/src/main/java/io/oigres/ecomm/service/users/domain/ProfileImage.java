package io.oigres.ecomm.service.users.domain;

import lombok.*;

import org.springframework.data.annotation.CreatedDate;

import io.oigres.ecomm.service.users.enums.ResourceStatusEnum;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "profile_images")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProfileImage extends DomainEntity {
    private static final long serialVersionUID = -4164258600038645847L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "image_url")
    private String imageURL;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ResourceStatusEnum status;
    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public boolean isDeleted() {
        return ResourceStatusEnum.DELETED.equals(this.status);
    }

}
