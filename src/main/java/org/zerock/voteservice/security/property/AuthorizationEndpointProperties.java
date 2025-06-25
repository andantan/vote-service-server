package org.zerock.voteservice.security.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Getter
@Setter
@Component
public class AuthorizationEndpointProperties {
    @Value("${springdoc.document.endpoint}")
    private String springdocDocumentEndpoint;

    @Value("${web-client.api.base-endpoint}")
    private String webClientApiBaseEndpoint;

    @Value("${blockchain-node.unicast.notification-endpoint}")
    private String blockchainNodeUnicastNotificationEndpoint;

    public RequestMatcher[] getPermittedRequestMatchers() {
        return new RequestMatcher[]{
                AntPathRequestMatcher.antMatcher(this.springdocDocumentEndpoint + "/**"),
                AntPathRequestMatcher.antMatcher(this.webClientApiBaseEndpoint + "/**"),
                AntPathRequestMatcher.antMatcher(this.blockchainNodeUnicastNotificationEndpoint + "/**")
        };
    }
}
