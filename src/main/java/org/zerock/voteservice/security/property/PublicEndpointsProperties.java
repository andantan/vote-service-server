package org.zerock.voteservice.security.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;

@Getter
@Setter
@Component
public class PublicEndpointsProperties {
    @Value("${springdoc.document.endpoint}")
    private String springdocDocumentEndpoint;   // /documents

    @Value("${command.api.base-endpoint}")
    private String commandApiBaseEndpoint;

    @Value("${web-client.api.user-endpoint}")
    private String webClientUserEndpoint;   // /user

    @Value("${blockchain-node.unicast.notification-endpoint}")
    private String blockchainNodeUnicastNotificationEndpoint;  // /unicast

    public String getRegisterEndpoint() {
        return this.webClientUserEndpoint + "/register";
    }

    public String getLoginEndpoint() {
        return this.webClientUserEndpoint + "/login";
    }

    public String getRefreshTokenEndpoint() {
        return this.webClientUserEndpoint + "/refresh-token";
    }

    public List<String> getPermittedEndpoints() {
        return List.of(
                "**/favicon.ico",
                this.getCommandApiBaseEndpoint() + "/**",
                this.getSpringdocDocumentEndpoint() + "/**",
                this.getRegisterEndpoint() + "/**",
                this.getLoginEndpoint() + "/**",
                this.getBlockchainNodeUnicastNotificationEndpoint() + "/**"
        );
    }

    public RequestMatcher[] getPermittedRequestMatchers() {
        return getPermittedEndpoints().stream()
                .map(AntPathRequestMatcher::antMatcher)
                .toArray(RequestMatcher[]::new);
    }
}
