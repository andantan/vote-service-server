package org.zerock.voteservice.adapter.out.grpc.stub.mongodbServer.blockchainData;

import domain.event.block.protocol.BlockEventServiceGrpc;
import domain.event.block.protocol.BlockCreatedEventRequest;
import domain.event.block.protocol.BlockCreatedEventResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class BlockEventServiceGrpcStub {
    private final BlockEventServiceGrpc.BlockEventServiceBlockingStub stub;

    public BlockEventServiceGrpcStub(
            @Value("${grpc.server.event.block.host}") String grpcBlockEventConnectionHost,
            @Value("${grpc.server.event.block.port}") int grpcBlockEventConnectionPort) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(grpcBlockEventConnectionHost, grpcBlockEventConnectionPort)
                .usePlaintext()
                .build();

        stub = BlockEventServiceGrpc.newBlockingStub(channel);
    }

    public BlockCreatedEventResponse reportBlockCreatedEvent(String topic, int transactionCount, int height) {
        BlockCreatedEventRequest request = BlockCreatedEventRequest.newBuilder()
                .setTopic(topic)
                .setTransactionCount(transactionCount)
                .setHeight(height)
                .build();

        return stub.reportBlockCreatedEvent(request);
    }
}
