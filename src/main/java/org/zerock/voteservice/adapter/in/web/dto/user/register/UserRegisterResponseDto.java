package org.zerock.voteservice.adapter.in.web.dto.user.register;

import lombok.*;
import java.time.LocalDateTime;

import org.zerock.voteservice.adapter.out.persistence.entity.UserEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterResponseDto {
    private Integer id;
    private String username;
    private String name;
    private String birthDate;
    private String email;
    private String phoneNumber;
    private String role;
    private LocalDateTime createdAt;

    public static UserRegisterResponseDto fromEntity(UserEntity userEntity) {
        return UserRegisterResponseDto.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .name(userEntity.getName())
                .birthDate(userEntity.getBirthDate())
                .email(userEntity.getEmail())
                .phoneNumber(userEntity.getPhoneNumber())
                .role(userEntity.getRole().name())
                .createdAt(userEntity.getCreatedAt())
                .build();
    }
}
