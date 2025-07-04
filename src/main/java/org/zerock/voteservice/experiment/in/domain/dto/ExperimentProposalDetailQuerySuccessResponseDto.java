package org.zerock.voteservice.experiment.in.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.zerock.voteservice.adapter.in.web.domain.schema.ProposalDetailSchema;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ExperimentProposalDetailQuerySuccessResponseDto extends ExperimentResponseDto {
    @JsonProperty("topic")
    private String topic;

    @JsonProperty("proposal")
    private ProposalDetailSchema proposal;
}
