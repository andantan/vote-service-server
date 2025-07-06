package org.zerock.voteservice.adapter.out.grpc.data;

import domain.event.pending.protocol.PendingExpiredEventResponse;
import lombok.Getter;
import org.zerock.voteservice.adapter.out.grpc.common.GrpcResponseData;

@Getter
public class GrpcPendingExpiredEventResponseData implements GrpcResponseData {
    Boolean success;
    String status;

    public GrpcPendingExpiredEventResponseData(PendingExpiredEventResponse response) {
        this.success = response.getCached();
        this.status = response.getStatus();
    }
}
