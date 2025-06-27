package org.zerock.voteservice.adapter.in.web.dto.user.register;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterResponseDto {
    private Integer id;
    private String username;
    private String realName;
    private String birthDate;
    private String gender;
    private String email;
    private String phoneNumber;
    private String role;
    private LocalDateTime createdAt;
}
