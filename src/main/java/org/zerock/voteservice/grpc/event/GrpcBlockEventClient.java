package org.zerock.voteservice.grpc.event;

import domain.event.block.protocol.CreatedBlockEventServiceGrpc;
import domain.event.block.protocol.CreatedBlockEvent;
import domain.event.block.protocol.ReportBlockEventResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class GrpcBlockEventClient {
    private final CreatedBlockEventServiceGrpc.CreatedBlockEventServiceBlockingStub stub;

    public GrpcBlockEventClient(
            @Value("${grpc.server.event.block.host}") String grpcBlockEventConnectionHost,
            @Value("${grpc.server.event.block.port}") int grpcBlockEventConnectionPort) {
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
