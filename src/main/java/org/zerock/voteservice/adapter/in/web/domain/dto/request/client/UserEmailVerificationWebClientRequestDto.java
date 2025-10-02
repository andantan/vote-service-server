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
public class UserEmailVerificationWebClientRequestDto implements UnauthenticatedRequestDto {
    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;

    @JsonProperty("real_name")
    private String realName;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("category")
    private String category;

    @Override
    public String identifier() {
        return String.format("AttemptingVerificationUsername:%s", username);
    }
}
