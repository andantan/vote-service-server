package org.zerock.voteservice.adapter.out.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "refresh_rotates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRefreshTokenRotateEntity {
    @Id
    @Column(
            name = "uid",
            nullable = false,
            updatable = false,
            unique = true
    )
    private Long uid;

    @Column(
            name = "username",
            nullable = false,
            updatable = false,
            unique = true,
            length = 50
    )
    private String username;

    @Column(
            name = "refresh",
            columnDefinition = "TEXT"
    )
    private String refresh;

    @Column(
            name = "expiration"
    )
    private Long expiration;
}
