package org.zerock.voteservice.adapter.in.web.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.zerock.voteservice.adapter.in.common.RequestDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequestDto implements RequestDto {
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
}
