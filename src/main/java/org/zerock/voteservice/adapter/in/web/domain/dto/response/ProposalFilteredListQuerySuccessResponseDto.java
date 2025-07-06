package org.zerock.voteservice.adapter.in.web.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;
import lombok.experimental.SuperBuilder;

import org.zerock.voteservice.adapter.in.common.extend.CommonResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.schema.ProposalResponseSchema;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProposalFilteredListQuerySuccessResponseDto extends CommonResponseDto {
    @JsonProperty("summarize")
    private Boolean summarize = false;

    @JsonProperty("expired")
    private Boolean expired = null;

    @JsonProperty("sort_order")
    private String sortOrder;

    @JsonProperty("sort_by")
    private String sortBy;

    @JsonProperty("skip")
    private Integer skip = 0;

    @JsonProperty("limit")
    private Integer limit = 15;

    @JsonProperty("proposal_list")
    private List<? extends ProposalResponseSchema> proposalList;

    @JsonProperty("proposal_list_length")
    private Integer proposalListLength = 0;
}
