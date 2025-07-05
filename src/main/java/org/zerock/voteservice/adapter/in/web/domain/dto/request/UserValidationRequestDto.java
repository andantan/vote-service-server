package org.zerock.voteservice.adapter.in.web.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.zerock.voteservice.adapter.in.common.RequestDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserValidationRequestDto implements RequestDto {
    @JsonProperty("uid")
    private Integer uid;

    @JsonProperty("user_hash")
    private String userHash;
}
