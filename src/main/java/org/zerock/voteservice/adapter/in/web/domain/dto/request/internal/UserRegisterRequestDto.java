package org.zerock.voteservice.adapter.in.web.domain.dto.request.internal;

import lombok.*;
import org.zerock.voteservice.adapter.in.common.RequestDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequestDto implements RequestDto {
    private String username;
    private String password;
    private String realName;
    private String srnPart;
    private String email;
    private String phoneNumber;
}
