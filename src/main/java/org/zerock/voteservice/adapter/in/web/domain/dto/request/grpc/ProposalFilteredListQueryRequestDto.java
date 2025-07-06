package org.zerock.voteservice.adapter.in.web.domain.dto.request.grpc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.zerock.voteservice.adapter.in.common.extend.GrpcRequestDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProposalFilteredListQueryRequestDto implements GrpcRequestDto {

    private Boolean summarize;
    private Boolean expired = null;
    private String sortOrder;
    private String sortBy;
    private Integer skip;
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
