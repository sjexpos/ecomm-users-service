package io.oigres.ecomm.service.users.domain;

import lombok.*;

import jakarta.persistence.*;

@Entity
@Table(name = "configs")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Config extends DomainEntity {
    private static final long serialVersionUID = -4164258600038645847L;
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "key")
    private String key;
    @Column(name = "value")
    private String value;
}
