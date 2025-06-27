package org.zerock.voteservice.adapter.out.persistence.entity;

import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
            updatable = false
    )
    private LocalDate birthDate;

    @Column(
            name = "gender",
            nullable = false,
            updatable = false,
            length = 1
    )
    private String gender;

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

    public static UserEntity newUserEntity(
            UserRegisterRequestDto dto, PasswordEncoder encoder
    ) throws IllegalArgumentException {
        UserEntity.UserEntityBuilder userBuilder = UserEntity.builder()
                .username(dto.getUsername())
                .password(encoder.encode(dto.getPassword()))
                .realName(dto.getRealName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .role(UserRole.USER);

        userBuilder = extractBirthDateAndGender(userBuilder, dto.getResidentRegistrationNumberPart());

        return userBuilder.build();
    }

    private static UserEntity.UserEntityBuilder extractBirthDateAndGender(
            UserEntity.UserEntityBuilder builder, String registrationNumber
    ) throws IllegalArgumentException {
        if (registrationNumber == null || registrationNumber.length() != 8 || registrationNumber.charAt(6) != '-') {
            throw new IllegalArgumentException("유효하지 않은 주민등록번호 형식입니다: " + registrationNumber + ". (YYMMDD-G 형식이어야 합니다)");
        }

        String birthDateStr = registrationNumber.substring(0, 6);
        char genderDigit = registrationNumber.charAt(7);

        LocalDate parsedBirthDate;
        String gender;

        try {
            String yearPrefix;

            switch (genderDigit) {
                case '1' -> { yearPrefix = "19"; gender = "M"; } // 1900년대 남성
                case '2' -> { yearPrefix = "19"; gender = "W"; } // 1900년대 여성
                case '3' -> { yearPrefix = "20"; gender = "M"; } // 2000년대 남성
                case '4' -> { yearPrefix = "20"; gender = "W"; } // 2000년대 여성
                default -> throw new IllegalArgumentException("유효하지 않은 성별 구분자입니다: " + genderDigit);
            }

            parsedBirthDate = LocalDate.parse(yearPrefix + birthDateStr, DateTimeFormatter.ofPattern("yyyyMMdd"));

        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("유효하지 않은 생년월일입니다: " + birthDateStr);
        }

        return builder.birthDate(parsedBirthDate).gender(gender);
    }
}
