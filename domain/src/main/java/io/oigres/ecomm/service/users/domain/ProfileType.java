package io.oigres.ecomm.service.users.domain;

import lombok.*;

import jakarta.persistence.*;

import io.oigres.ecomm.service.users.enums.ProfileTypeEnum;

@Entity
@Table(name = "profile_types")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProfileType extends DomainEntity {
    private static final long serialVersionUID = -4164258600038645847L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "profile")
    private ProfileTypeEnum profile;
}
