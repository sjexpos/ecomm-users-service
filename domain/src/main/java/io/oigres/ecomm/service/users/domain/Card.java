package io.oigres.ecomm.service.users.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cards")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Card extends DomainEntity {
    private static final long serialVersionUID = -4164258600038645847L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "mmj_card")
    private Boolean mmjCard;
    @Column(name = "id_card")
    private Boolean idCard;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "card_image_id")
    private CardImage cardImage;
    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
