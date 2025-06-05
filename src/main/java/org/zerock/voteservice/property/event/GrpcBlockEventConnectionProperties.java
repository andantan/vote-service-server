package org.zerock.voteservice.property.event;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "grpc.server.event.block")
@Data
public class GrpcBlockEventConnectionProperties {
    private String host;
    private int port;
}
