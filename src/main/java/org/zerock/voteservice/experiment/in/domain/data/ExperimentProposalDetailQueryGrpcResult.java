package org.zerock.voteservice.experiment.in.domain.data;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.zerock.voteservice.experiment.out.status.ExperimentProposalDetailQueryStatus;

@Getter
@Setter
@SuperBuilder
//@NoArgsConstructor
@AllArgsConstructor
public class ExperimentProposalDetailQueryGrpcResult
        extends ExperimentAbstractGrpcResult<ExperimentProposalDetailQueryStatus, ExperimentProposalDetailQueryGrpcResponseData> {
}
