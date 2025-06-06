package org.zerock.voteservice.grpc.event;

import domain.event.block.protocol.CreatedBlockEventServiceGrpc;
import domain.event.block.protocol.CreatedBlockEvent;
import domain.event.block.protocol.ReportBlockEventResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public class GrpcBlockEventClient {
    private final CreatedBlockEventServiceGrpc.CreatedBlockEventServiceBlockingStub stub;

    public GrpcBlockEventClient(String grpcBlockEventConnectionHost, int grpcBlockEventConnectionPort) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(grpcBlockEventConnectionHost, grpcBlockEventConnectionPort)
                .usePlaintext()
                .build();

        stub = CreatedBlockEventServiceGrpc.newBlockingStub(channel);
    }

    public Map<String, Object> reportCreatedBlockEvent(String topic, int height) {
        log.info("#[gRPC]#[To  : MongoDB-Cache-Server] ReportCreatedBlockEvent request: topic='{}', height={}",
                topic, height
        );

        CreatedBlockEvent request = CreatedBlockEvent.newBuilder()
                .setTopic(topic)
                .setHeight(height)
                .build();

        ReportBlockEventResponse response = stub.reportCreatedBlockEvent(request);

        log.info("#[gRPC]#[From: MongoDB-Cache-Server] ReportCreatedBlockEvent Response: Success={}, Message='{}'",
                response.getSuccess(),
                response.getMessage()
        );

        Map<String, Object> responseMap = new HashMap<>();

        responseMap.put("success", response.getSuccess());
        responseMap.put("message", response.getMessage());

        return responseMap;
    }
}
