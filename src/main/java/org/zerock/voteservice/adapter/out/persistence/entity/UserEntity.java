package org.zerock.voteservice.adapter.out.persistence.entity;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.zerock.voteservice.adapter.in.web.dto.user.register.UserRegisterRequestDto;
import org.zerock.voteservice.adapter.in.web.dto.user.role.UserRole;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(
            name = "id",
            nullable = false,
            updatable = false
    )
    private Integer id;

    @Column(
            name = "username",
            nullable = false,
            updatable = false,
            unique = true,
            length = 50
    )
    private String username;

    @Column(
            name = "password",
            nullable = false
    )
    private String password;

    @Column(
            name = "real_name",
            nullable = false,
            length = 100
    )
    private String realName;

    @Column(
            name = "birth_date",
            nullable = false,
            updatable = false,
            length = 8  // format = "YYMMDD-G" ( Example: 001209-3 )
    )
    private String birthDate;

    @Column(
            name = "email",
            nullable = false,
            updatable = false,
            unique = true,
            length = 100
    )
    private String email;

    @Column(
            name = "phone_number",
            nullable = false,
            unique = true,
            length = 20
    )
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "role",
            nullable = false,
            updatable = false,
            length = 30
    )
    private UserRole role;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column(
            name = "updated_at",
            nullable = false
    )
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public static UserEntity newUserEntity(UserRegisterRequestDto dto, PasswordEncoder encoder) {
        return UserEntity.builder()
                .username(dto.getUsername())
                .password(encoder.encode(dto.getPassword()))
                .realName(dto.getRealName())
                .birthDate(dto.getBirthDate())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .role(UserRole.USER)
                .build();
    }
}
