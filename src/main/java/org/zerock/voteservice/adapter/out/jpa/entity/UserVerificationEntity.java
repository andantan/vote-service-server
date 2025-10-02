package org.zerock.voteservice.adapter.out.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "user_verification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVerificationEntity {
    @Id
    @Column(
            name = "username",
            nullable = false,
            updatable = false,
            unique = true,
            length = 50
    )
    private String username;

    @Column(
            name = "uid"
    )
    private Long uid;

    @Column(
            name = "email",
            nullable = false,
            updatable = false,
            length = 100
    )
    private String email;

    @Column(
            name = "phone_number",
            nullable = false,
            updatable = false,
            unique = true,
            length = 20
    )
    private String phoneNumber;

    @Column(
            name = "real_name",
            nullable = false,
            updatable = false,
            length = 100
    )
    private String realName;

    @Column(
            name = "code",
            nullable = false
    )
    private String code;

    @Column(
            name = "expiration",
            nullable = false,
            updatable = false
    )
    private Date expiration;

    @Column(
            name = "veritification_category",
            nullable = false,
            length = 30
    )
    private String category;
}
