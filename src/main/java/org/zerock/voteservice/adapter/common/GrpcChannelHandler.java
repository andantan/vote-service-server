package org.zerock.voteservice.adapter.common;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class GrpcChannelHandler {

    private GrpcChannelHandler() {}

    public static ManagedChannel getPlainedManagedChannel(
            String layerName, String ServiceName, String host, int port
    ) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();

        String logMessage = String.format("[%s:%-5d] [%s-%s] gRPC channel initialized", host, port, layerName, ServiceName);
        log.info(logMessage);

        return channel;
    }
}
