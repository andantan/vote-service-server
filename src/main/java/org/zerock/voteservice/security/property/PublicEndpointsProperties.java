package org.zerock.voteservice.security.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Component
public class PublicEndpointsProperties {
    @Value("${springdoc.document.endpoint}")
    private String springdocDocumentEndpoint;   // documents

    @Value("${web-client.api.user-endpoint}")
    private String webClientUserEndpoint;

    @Value("${blockchain-node.unicast.notification-endpoint}")
    private String blockchainNodeUnicastNotificationEndpoint;

    public String getRegisterEndpoint() {
        return this.webClientUserEndpoint + "/register";
    }

    public String getLoginEndpoint() {
        return this.webClientUserEndpoint + "/login";
    }

    public List<String> getPermittedEndpoints() {
        return List.of(
                this.getSpringdocDocumentEndpoint(),
                this.getRegisterEndpoint(),
                this.getLoginEndpoint(),
                this.getBlockchainNodeUnicastNotificationEndpoint()
        );
    }

    public RequestMatcher[] getPermittedRequestMatchers() {
        return getPermittedEndpoints().stream()
                .map(endpoint -> AntPathRequestMatcher.antMatcher(endpoint + "/**"))
                .toArray(RequestMatcher[]::new);
    }

    public List<String> getExcludedJwtAuthenticationEndpoints() {
        return this.getDefaultExcludedEndpoints();
    }

    public List<String> getExcludedUserHashFilterEndpoints() {
        return this.getDefaultExcludedEndpoints();
    }

    private List<String> getDefaultExcludedEndpoints() {
        List<String> excludedEndpoints = new ArrayList<>();

        excludedEndpoints.addAll(this.getExcludedUserEndpointList());
        excludedEndpoints.addAll(this.getExcludedDocumentEndpointList());
        excludedEndpoints.addAll(this.getExcludedBlockchainNotificationEndpointList());

        return excludedEndpoints;
    }

    private List<String> getExcludedUserEndpointList() {
        return List.of(
                this.getRegisterEndpoint(),
                this.getLoginEndpoint()
        );
    }

    private List<String> getExcludedDocumentEndpointList() {
        return List.of(
                this.getSpringdocDocumentEndpoint() + "/swagger-ui/swagger-initializer.js",
                this.getSpringdocDocumentEndpoint() + "/swagger-ui/index.html",
                this.getSpringdocDocumentEndpoint() + "/api/swagger-config",
                this.getSpringdocDocumentEndpoint() + "/api"
        );
    }

    private List<String> getExcludedBlockchainNotificationEndpointList() {
        return List.of(
                this.getBlockchainNodeUnicastNotificationEndpoint() + "/new-block",
                this.getBlockchainNodeUnicastNotificationEndpoint() + "/expired-pending"
        );
    }
}
