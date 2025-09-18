package org.zerock.voteservice.adapter.in.web.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.zerock.voteservice.adapter.in.common.extend.CommonResponseDto;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoSuccessResponseDto extends CommonResponseDto {
    @JsonProperty("uid")
    private Long uid;

    @JsonProperty("user_hash")
    private String userHash;

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("real_name")
    private String realName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;
}
