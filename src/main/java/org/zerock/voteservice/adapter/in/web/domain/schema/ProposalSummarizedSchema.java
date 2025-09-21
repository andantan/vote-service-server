package org.zerock.voteservice.adapter.in.web.domain.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProposalSummarizedSchema implements ProposalResponseSchema {
    @JsonProperty("topic")
    private String topic;

    @JsonProperty("proposer")
    private String proposer;

    @JsonProperty("expired")
    private Boolean expired;

    @JsonProperty("expired_at")
    private LocalDateTime expiredAt;
}
