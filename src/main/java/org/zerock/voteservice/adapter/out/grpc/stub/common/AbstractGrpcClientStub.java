package org.zerock.voteservice.adapter.out.grpc.stub.common;

import io.grpc.ManagedChannel;
import jakarta.annotation.PreDestroy;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.TimeUnit;

@Log4j2
public abstract class AbstractGrpcClientStub implements GrpcGracefullyShutDownHook {

    protected final ManagedChannel channel;
    protected final String layerName;
    protected final String serviceName;
    protected final String grpcHost;
    protected final int grpcPort;
    protected final String logPrefix;

    public AbstractGrpcClientStub(
            String layerName,
            String serviceName,
            String grpcHost,
            int grpcPort
    ) {
        this.layerName = layerName;
        this.serviceName = serviceName;
        this.grpcHost = grpcHost;
        this.grpcPort = grpcPort;

        this.channel = GrpcChannelHandler.getPlainedManagedChannel(layerName, serviceName, grpcHost, grpcPort);
        this.logPrefix  = String.format("[%s:%-5d] [%s-%s] ", grpcHost, grpcPort, layerName, serviceName);

        log.info("{}gRPC stub initialized for service", logPrefix);
    }

    @PreDestroy
    @Override
    public void shutdown() {
        log.info("{}Shutting down gRPC channel for service.", logPrefix);

        try {
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.warn("{}gRPC channel shutdown interrupted. Forcing shutdown.", logPrefix);

            channel.shutdownNow();
            Thread.currentThread().interrupt();
        }

        log.info("{}gRPC channel successfully shut down.", logPrefix);
    }
}
