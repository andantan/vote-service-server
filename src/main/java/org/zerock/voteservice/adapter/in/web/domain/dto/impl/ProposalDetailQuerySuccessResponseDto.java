package org.zerock.voteservice.adapter.in.web.domain.dto.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.zerock.voteservice.adapter.in.web.domain.dto.CommonResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.schema.ProposalDetailSchema;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProposalDetailQuerySuccessResponseDto extends CommonResponseDto {
    @JsonProperty("topic")
    private String topic;

    @JsonProperty("proposal")
    private ProposalDetailSchema proposal;
}
