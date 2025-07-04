package org.zerock.voteservice.adapter.out.grpc.stub;

import domain.event.block.protocol.BlockEventServiceGrpc;
import domain.event.block.protocol.BlockCreatedEventRequest;
import domain.event.block.protocol.BlockCreatedEventResponse;
import io.grpc.StatusRuntimeException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.zerock.voteservice.adapter.common.GrpcExceptionHandler;
import org.zerock.voteservice.adapter.out.grpc.common.AbstractGrpcClientStub;


@Log4j2
@Service
public class BlockEventServiceGrpcStub extends AbstractGrpcClientStub {
    private final BlockEventServiceGrpc.BlockEventServiceBlockingStub stub;

    public BlockEventServiceGrpcStub(
            @Value("${grpc.server.event.block.host}") String host,
            @Value("${grpc.server.event.block.port}") int port
    ) {
        super("L3", BlockEventServiceGrpc.class.getSimpleName(), host, port);

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
                    e, layerName, serviceName, rpcName, grpcHost, grpcPort, request
            );
        }
    }
}
