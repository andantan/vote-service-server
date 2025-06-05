package org.zerock.voteservice.grpc.event;

import domain.event.pending.protocol.ExpiredPendingEventServiceGrpc;
import domain.event.pending.protocol.ExpiredPendingEvent;
import domain.event.pending.protocol.ReportPendingEventResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.HashMap;
import java.util.Map;

public class GrpcPendingEventClient {
   private final ExpiredPendingEventServiceGrpc.ExpiredPendingEventServiceBlockingStub stub;

   public GrpcPendingEventClient(String grpcPendingEventConnectionHost, int grpcPendingEventConnectionPort) {
       ManagedChannel channel = ManagedChannelBuilder
               .forAddress(grpcPendingEventConnectionHost, grpcPendingEventConnectionPort)
               .usePlaintext()
               .build();

       stub = ExpiredPendingEventServiceGrpc.newBlockingStub(channel);
   }

   public Map<String, Object> reportExpiredPendingEvent(String topic, int count, Map<String, Long> options) {
       ExpiredPendingEvent request = ExpiredPendingEvent.newBuilder()
               .setTopic(topic)
               .setCount(count)
               .putAllOptions(options)
               .build();

       ReportPendingEventResponse response = stub.reportExpiredPendingEvent(request);
       Map<String, Object> responseMap = new HashMap<>();

       responseMap.put("success", response.getSuccess());
       responseMap.put("message", response.getMessage());

       return responseMap;
   }
}
