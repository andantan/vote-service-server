package org.zerock.voteservice.grpc.event;

import domain.event.block.protocol.CreatedBlockEventServiceGrpc;
import domain.event.block.protocol.CreatedBlockEvent;
import domain.event.block.protocol.ReportBlockEventResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.HashMap;
import java.util.Map;

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
        CreatedBlockEvent request = CreatedBlockEvent.newBuilder()
                .setTopic(topic)
                .setHeight(height)
                .build();

        ReportBlockEventResponse response = stub.reportCreatedBlockEvent(request);
        Map<String, Object> responseMap = new HashMap<>();

        responseMap.put("success", response.getSuccess());
        responseMap.put("message", response.getMessage());

        return responseMap;
    }
}
