package org.zerock.voteservice.grpc.event;

import domain.event.pending.protocol.ExpiredPendingEventServiceGrpc;
import domain.event.pending.protocol.ExpiredPendingEvent;
import domain.event.pending.protocol.ReportPendingEventResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public class GrpcPendingEventClient {
   private final ExpiredPendingEventServiceGrpc.ExpiredPendingEventServiceBlockingStub stub;

   public GrpcPendingEventClient(String grpcPendingEventConnectionHost, int grpcPendingEventConnectionPort) {
       ManagedChannel channel = ManagedChannelBuilder
               .forAddress(grpcPendingEventConnectionHost, grpcPendingEventConnectionPort)
               .usePlaintext()
               .build();

       stub = ExpiredPendingEventServiceGrpc.newBlockingStub(channel);
   }

   public ReportPendingEventResponse reportExpiredPendingEvent(String topic, int count, Map<String, Integer> options) {
       ExpiredPendingEvent request = ExpiredPendingEvent.newBuilder()
               .setTopic(topic)
               .setCount(count)
               .putAllOptions(options)
               .build();

       return stub.reportExpiredPendingEvent(request);
   }
}
