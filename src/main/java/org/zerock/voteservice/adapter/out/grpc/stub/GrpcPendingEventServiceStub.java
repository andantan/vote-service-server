package org.zerock.voteservice.adapter.out.grpc.stub;

import domain.event.pending.protocol.PendingEventServiceGrpc;
import domain.event.pending.protocol.PendingExpiredEventRequest;
import domain.event.pending.protocol.PendingExpiredEventResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.zerock.voteservice.adapter.out.grpc.common.AbstractGrpcClientStub;

import java.util.Map;

@Log4j2
@Component
public class GrpcPendingEventServiceStub extends AbstractGrpcClientStub {

    private final PendingEventServiceGrpc.PendingEventServiceBlockingStub stub;
   public GrpcPendingEventServiceStub(
           @Value("${grpc.server.event.pending.host}") String host,
           @Value("${grpc.server.event.pending.port}") int port
   ) {
       super("L4", PendingEventServiceGrpc.class.getSimpleName(), host, port);

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

       return stub.reportPendingExpiredEvent(request);
   }
}
