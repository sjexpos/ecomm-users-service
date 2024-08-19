package io.oigres.ecomm.service.users.domain;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fav_user_products")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FavouriteProduct extends DomainEntity {
    private static final long serialVersionUID = -4164258600038645847L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "product_id")
    private Integer productId;

}
