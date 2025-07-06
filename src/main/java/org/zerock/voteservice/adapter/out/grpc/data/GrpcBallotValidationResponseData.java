package org.zerock.voteservice.adapter.out.grpc.data;

import domain.event.ballot.create.protocol.BallotValidateEventResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zerock.voteservice.adapter.out.grpc.common.GrpcResponseData;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrpcBallotValidationResponseData implements GrpcResponseData {
    Boolean success;
    String status;

    public GrpcBallotValidationResponseData(BallotValidateEventResponse response) {
        this.success = response.getValidation();
        this.status = response.getStatus();
    }
}
