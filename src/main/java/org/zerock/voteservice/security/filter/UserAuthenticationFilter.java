package org.zerock.voteservice.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.zerock.voteservice.adapter.in.web.dto.user.authentication.UserAuthenticationDetails;
import org.zerock.voteservice.adapter.in.web.dto.user.authentication.UserAuthenticationRequestDto;
import java.io.IOException;

@Log4j2
public class UserAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    public static final String ATTEMPTED_USERNAME_ATTRIBUTE = "attemptedUsername";

    private final ObjectMapper objectMapper;

    public UserAuthenticationFilter(ObjectMapper objectMapper) {
        super();
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response
    ) throws AuthenticationException {
        String logPrefix = "[Unauth] ";
        UserAuthenticationRequestDto dto;

        try {
            dto = this.objectMapper.readValue(request.getInputStream(), UserAuthenticationRequestDto.class);

            request.setAttribute(ATTEMPTED_USERNAME_ATTRIBUTE, dto.getUsername());
            logPrefix = String.format("[Username:%s] ", dto.getUsername());

            log.debug("{}Parsed authentication request for username", logPrefix);
        } catch (IOException e) {
            log.error("{}Error parsing JSON authentication request from client: {}", logPrefix, e.getMessage(), e);
            throw new AuthenticationServiceException("Error parsing authentication request JSON", e);
        }

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());

        log.debug("{}Created UsernamePasswordAuthenticationToken", logPrefix);

        return getAuthenticationManager().authenticate(token);
    }
}
