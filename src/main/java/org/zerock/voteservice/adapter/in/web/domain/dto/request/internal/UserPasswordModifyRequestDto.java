package org.zerock.voteservice.adapter.in.web.domain.dto.request.internal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPasswordModifyRequestDto {
    private Long uid;
    private String username;
    private String newPassword;
}
