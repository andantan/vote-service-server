package org.zerock.voteservice.adapter.in.web.domain.dto.request.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.zerock.voteservice.adapter.in.common.extend.UnauthenticatedRequestDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPasswordResetWebClientRequestDto implements UnauthenticatedRequestDto {
    @JsonProperty("uid")
    private Long uid;

    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;

    @JsonProperty("real_name")
    private String realName;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("new_password")
    private String newPassword;

    @JsonProperty("verification_code")
    private String verificationCode;

    @Override
    public String identifier() {
        return String.format("UID:%s ", uid.toString());
    }
}
