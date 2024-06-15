package io.oigres.ecomm.service.users.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "countries")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Country extends DomainEntity {
    private static final long serialVersionUID = -4164258600038645847L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "short_name")
    private String shortName;
    @OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
    private Set<State> states;
}
