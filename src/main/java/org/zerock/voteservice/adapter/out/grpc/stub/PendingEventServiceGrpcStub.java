package org.zerock.voteservice.adapter.out.grpc.stub;

import domain.event.pending.protocol.PendingEventServiceGrpc;
import domain.event.pending.protocol.PendingExpiredEventRequest;
import domain.event.pending.protocol.PendingExpiredEventResponse;
import io.grpc.StatusRuntimeException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.zerock.voteservice.adapter.out.grpc.stub.common.AbstractGrpcClientStub;
import org.zerock.voteservice.adapter.common.GrpcExceptionHandler;

import java.util.Map;

@Log4j2
@Service
public class PendingEventServiceGrpcStub extends AbstractGrpcClientStub {

    private final PendingEventServiceGrpc.PendingEventServiceBlockingStub stub;
   public PendingEventServiceGrpcStub(
           @Value("${grpc.server.event.pending.host}") String host,
           @Value("${grpc.server.event.pending.port}") int port
   ) {
       super("L3", PendingEventServiceGrpc.class.getSimpleName(), host, port);

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
                   e, layerName, serviceName, rpcName, grpcHost, grpcPort, request
           );
       }
   }
}
