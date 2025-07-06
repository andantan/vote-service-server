package org.zerock.voteservice.adapter.out.grpc.data;

import domain.event.block.protocol.BlockCreatedEventResponse;
import lombok.Getter;
import org.zerock.voteservice.adapter.out.grpc.common.GrpcResponseData;

@Getter
public class GrpcBlockCreatedEventResponseData implements GrpcResponseData {
    Boolean success;
    String status;

    public GrpcBlockCreatedEventResponseData(BlockCreatedEventResponse response) {
        this.success = response.getCached();
        this.status = response.getStatus();
    }
}
