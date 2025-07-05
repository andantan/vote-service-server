package org.zerock.voteservice.adapter.in.web.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

import org.zerock.voteservice.adapter.in.common.RequestDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProposalFilteredListQueryRequestDto implements RequestDto {
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

    public Boolean isSortOrderFiltered() {
        return this.sortOrder != null;
    }

    public Boolean isExpiredFiltered() {
        return this.expired != null;
    }

    public Boolean isSortByFiltered() {
        return this.sortBy != null;
    }

    public Boolean isSortFiltered() {
        return this.isSortOrderFiltered() && this.isSortByFiltered();
    }
}
