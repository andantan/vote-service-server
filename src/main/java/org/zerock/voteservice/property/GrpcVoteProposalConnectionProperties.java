package org.zerock.voteservice.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "grpc.server.vote.proposal")
@Data
public class GrpcVoteProposalConnectionProperties {
    private String host;
    private int port;
}
