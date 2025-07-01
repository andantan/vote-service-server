package org.zerock.voteservice.adapter.out.grpc.stub.mongodbServer.blockchainData;

import domain.event.block.protocol.BlockEventServiceGrpc;
import domain.event.block.protocol.BlockCreatedEventRequest;
import domain.event.block.protocol.BlockCreatedEventResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.zerock.voteservice.adapter.common.GrpcChannelHandler;
import org.zerock.voteservice.adapter.common.GrpcExceptionHandler;

@Log4j2
@Service
public class BlockEventServiceGrpcStub {
    private static final String SERVICE_NAME = BlockEventServiceGrpc.class.getSimpleName();
    private static final String LAYER_NAME = "L3";

    private final BlockEventServiceGrpc.BlockEventServiceBlockingStub stub;
    private final String grpcHost;
    private final int grpcPort;

    public BlockEventServiceGrpcStub(
            @Value("${grpc.server.event.block.host}") String host,
            @Value("${grpc.server.event.block.port}") int port) {
        this.grpcHost = host;
        this.grpcPort = port;

        ManagedChannel channel = GrpcChannelHandler.getPlainedManagedChannel(LAYER_NAME, SERVICE_NAME, host, port);

        stub = BlockEventServiceGrpc.newBlockingStub(channel);
    }

    public BlockCreatedEventResponse reportBlockCreatedEvent(
            String topic, int transactionCount, int height
    ) throws RuntimeException {
        BlockCreatedEventRequest request = BlockCreatedEventRequest.newBuilder()
                .setTopic(topic)
                .setTransactionCount(transactionCount)
                .setHeight(height)
                .build();

        try {
            return stub.reportBlockCreatedEvent(request);
        } catch (StatusRuntimeException e) {
            String rpcName = Thread.currentThread().getStackTrace()[1].getMethodName();

            throw GrpcExceptionHandler.mapStatusRuntimeException(
                    e, LAYER_NAME, SERVICE_NAME, rpcName, grpcHost, grpcPort, request
            );
        }
    }
}
