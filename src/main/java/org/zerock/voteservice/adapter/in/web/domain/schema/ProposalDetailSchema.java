package org.zerock.voteservice.adapter.in.web.domain.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProposalDetailSchema implements ProposalResponseSchema {
    @JsonProperty("topic")
    private String topic;

    @JsonProperty("proposer")
    private String proposer;

    @JsonProperty("duration")
    private Integer duration;

    @JsonProperty("expired")
    private Boolean expired;

    @JsonProperty("block_heights")
    private List<BlockHeightSchema> blockHeights;

    @JsonProperty("result")
    private ResultSchema result;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("expired_at")
    private LocalDateTime expiredAt;

    @JsonProperty("options")
    private List<String> options;
}
