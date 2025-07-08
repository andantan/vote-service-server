package org.zerock.voteservice.adapter.out.grpc.stub;

import domain.event.admin.L4.protocol.L4CommandsGrpc;
import domain.event.admin.L4.protocol.L4HealthCheckRequest;
import domain.event.admin.L4.protocol.L4HealthCheckResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.zerock.voteservice.adapter.out.grpc.common.AbstractGrpcClientStub;

@Log4j2
@Component
public class GrpcL4CommandServiceStub extends AbstractGrpcClientStub {

    private final L4CommandsGrpc.L4CommandsBlockingStub stub;

    public GrpcL4CommandServiceStub(
            @Value("${grpc.server.admin.command.host}") String host,
            @Value("${grpc.server.admin.command.port}") int port
    ) {
        super("L4", GrpcL4CommandServiceStub.class.getSimpleName(), host, port);

        stub = L4CommandsGrpc.newBlockingStub(channel);
    }

    public L4HealthCheckResponse checkHealth(
            String ping
    ) {
        L4HealthCheckRequest request = L4HealthCheckRequest.newBuilder().setPing(ping).build();

        return stub.checkHealth(request);
    }

}
