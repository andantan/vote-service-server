package org.zerock.voteservice.experiment.in.domain.dto;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExperimentProposalDetailQueryRequestDto implements ExperimentRequestDto {
    @JsonProperty("topic")
    private String topic;
}
