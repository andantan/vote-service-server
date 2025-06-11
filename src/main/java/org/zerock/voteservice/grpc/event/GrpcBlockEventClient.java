package org.zerock.voteservice.grpc.event;

import domain.event.block.protocol.CreatedBlockEventServiceGrpc;
import domain.event.block.protocol.CreatedBlockEvent;
import domain.event.block.protocol.ReportBlockEventResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.log4j.Log4j2;
import org.zerock.voteservice.dto.event.BlockCreatedEventDto;

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

    public ReportBlockEventResponse reportCreatedBlockEvent(String topic, int length, int height) {
        CreatedBlockEvent request = CreatedBlockEvent.newBuilder()
                .setTopic(topic)
                .setLength(length)
                .setHeight(height)
                .build();

        return stub.reportCreatedBlockEvent(request);
    }
}
