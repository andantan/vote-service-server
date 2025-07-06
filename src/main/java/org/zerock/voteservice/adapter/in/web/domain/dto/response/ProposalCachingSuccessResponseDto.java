package org.zerock.voteservice.adapter.in.web.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.zerock.voteservice.adapter.in.common.extend.CommonResponseDto;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProposalCachingSuccessResponseDto extends CommonResponseDto {
    @JsonProperty("topic")
    private String topic;

    @JsonProperty("duration")
    private Integer duration;

    @JsonProperty("options")
    private List<String> options;
}
