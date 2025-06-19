package org.zerock.voteservice.adapter.out.grpc.stub.mongodbServer.blockchainData;

import domain.event.pending.protocol.PendingEventServiceGrpc;
import domain.event.pending.protocol.PendingExpiredEventRequest;
import domain.event.pending.protocol.PendingExpiredEventResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Log4j2
@Service
public class PendingEventServiceGrpcStub {
   private final PendingEventServiceGrpc.PendingEventServiceBlockingStub stub;

   public PendingEventServiceGrpcStub(
           @Value("${grpc.server.event.pending.host}") String grpcPendingEventConnectionHost,
           @Value("${grpc.server.event.pending.port}") int grpcPendingEventConnectionPort
   ) {
       ManagedChannel channel = ManagedChannelBuilder
               .forAddress(grpcPendingEventConnectionHost, grpcPendingEventConnectionPort)
               .usePlaintext()
               .build();

       stub = PendingEventServiceGrpc.newBlockingStub(channel);
   }

   public PendingExpiredEventResponse reportPendingExpiredEvent(String topic, int count, Map<String, Integer> options) {
       PendingExpiredEventRequest request = PendingExpiredEventRequest.newBuilder()
               .setTopic(topic)
               .setCount(count)
               .putAllOptions(options)
               .build();

       return stub.reportPendingExpiredEvent(request);
   }
}
