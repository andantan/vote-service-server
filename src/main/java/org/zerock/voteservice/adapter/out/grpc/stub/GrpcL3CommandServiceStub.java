package org.zerock.voteservice.adapter.out.grpc.stub;

import domain.event.admin.L3.protocol.L3CommandsGrpc;
import domain.event.admin.L3.protocol.L3HealthCheckRequest;
import domain.event.admin.L3.protocol.L3HealthCheckResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.zerock.voteservice.adapter.out.grpc.common.AbstractGrpcClientStub;

@Log4j2
@Component
public class GrpcL3CommandServiceStub extends AbstractGrpcClientStub {

    private final L3CommandsGrpc.L3CommandsBlockingStub stub;

    public GrpcL3CommandServiceStub(
            @Value("${grpc.server.event.admin.host}") String host,
            @Value("${grpc.server.event.admin.port}") int port
    ) {
        super("L3", GrpcL3CommandServiceStub.class.getSimpleName(), host, port);

        stub = L3CommandsGrpc.newBlockingStub(channel);
    }

    public L3HealthCheckResponse checkHealth(
            String ping
    ) throws RuntimeException {
        L3HealthCheckRequest request = L3HealthCheckRequest.newBuilder().setPing(ping).build();

        return stub.checkHealth(request);
    }
}
