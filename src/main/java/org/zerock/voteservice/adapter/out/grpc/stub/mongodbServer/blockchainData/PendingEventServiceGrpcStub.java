package org.zerock.voteservice.adapter.out.grpc.stub.mongodbServer.blockchainData;

import domain.event.pending.protocol.PendingEventServiceGrpc;
import domain.event.pending.protocol.PendingExpiredEventRequest;
import domain.event.pending.protocol.PendingExpiredEventResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.zerock.voteservice.adapter.common.GrpcChannelHandler;
import org.zerock.voteservice.adapter.common.GrpcExceptionHandler;

import java.util.Map;

@Log4j2
@Service
public class PendingEventServiceGrpcStub {
    private static final String SERVICE_NAME = PendingEventServiceGrpc.class.getSimpleName();
    private static final String LAYER_NAME = "L3";

    private final PendingEventServiceGrpc.PendingEventServiceBlockingStub stub;
    private final String grpcHost;
    private final int grpcPort;

   public PendingEventServiceGrpcStub(
           @Value("${grpc.server.event.pending.host}") String host,
           @Value("${grpc.server.event.pending.port}") int port
   ) {
       this.grpcHost = host;
       this.grpcPort = port;

       ManagedChannel channel = GrpcChannelHandler.getPlainedManagedChannel(LAYER_NAME, SERVICE_NAME, host, port);

       stub = PendingEventServiceGrpc.newBlockingStub(channel);
   }

   public PendingExpiredEventResponse reportPendingExpiredEvent(
           String topic, int count, Map<String, Integer> options
   ) throws RuntimeException {
       PendingExpiredEventRequest request = PendingExpiredEventRequest.newBuilder()
               .setTopic(topic)
               .setCount(count)
               .putAllOptions(options)
               .build();

       try {
           return stub.reportPendingExpiredEvent(request);
       } catch (StatusRuntimeException e) {
           String rpcName = Thread.currentThread().getStackTrace()[1].getMethodName();

           throw GrpcExceptionHandler.mapStatusRuntimeException(
                   e, LAYER_NAME, SERVICE_NAME, rpcName, grpcHost, grpcPort, request
           );
       }
   }
}
