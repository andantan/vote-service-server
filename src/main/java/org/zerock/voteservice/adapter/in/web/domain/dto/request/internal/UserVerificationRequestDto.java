package org.zerock.voteservice.adapter.in.web.domain.dto.request.internal;

import lombok.*;
import org.zerock.voteservice.adapter.in.common.RequestDto;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVerificationRequestDto implements RequestDto {
    private Long uid;
    private String username;
    private String realname;
    private String email;
    private String phoneNumber;
    private String code;
    private String category;
}
