package org.zerock.voteservice.property.event;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "grpc.server.event.pending")
@Data
public class GrpcPendingEventConnectionProperties {
    private String host;
    private int port;
}
