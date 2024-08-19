package io.oigres.ecomm.service.users.domain;

import lombok.*;

import io.oigres.ecomm.service.users.enums.ResourceStatusEnum;

import jakarta.persistence.*;

@Entity
@Table(name = "card_images")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CardImage extends AbstractHistoryEntity {
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

}
