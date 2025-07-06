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
public class UserRegisterWebClientRequestDto implements UnauthenticatedRequestDto {
    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("real_name")
    private String realName;

    @JsonProperty("srn_part")
    private String srnPart;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @Override
    public String identifier() {
        return String.format("AttemptingRegisterUsername:%s", username);
    }
}
