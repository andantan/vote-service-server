package org.zerock.voteservice.adapter.in.web.domain.dto.request.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

import org.zerock.voteservice.adapter.in.common.extend.RestApiRequestDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProposalFilteredListQueryWebClientRequestDto implements RestApiRequestDto {
    @JsonProperty("summarize")
    private Boolean summarize;

    @JsonProperty("expired")
    private Boolean expired = null;

    @JsonProperty("sort_order")
    private String sortOrder;

    @JsonProperty("sort_by")
    private String sortBy;

    @JsonProperty("skip")
    private Integer skip;

    @JsonProperty("limit")
    private Integer limit;
}
