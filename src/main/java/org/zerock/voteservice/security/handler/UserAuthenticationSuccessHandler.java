package org.zerock.voteservice.security.handler;
;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.zerock.voteservice.adapter.in.web.dto.user.authentication.UserAuthenticationDetails;
import org.zerock.voteservice.security.jwt.JwtUtil;


@Log4j2
public class UserAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;

    public UserAuthenticationSuccessHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        log.info("------------UserAuthenticationSuccessHandler::onAuthenticationSuccess------------");

        UserAuthenticationDetails userDetails = (UserAuthenticationDetails) authentication.getPrincipal();

        Integer uid = userDetails.getUid();
        String username = userDetails.getUsername();
        String userHash = userDetails.getUserHash();
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("사용자에게 할당된 권한이 없습니다."));

        String token = jwtUtil.createJwt(uid, username, userHash, role);

        response.addHeader("Authorization", "Bearer " + token);

        log.info("Authorization successful: User '{}', UID '{}', Role '{}'. JWT has been generated.",
                username, uid, role);
    }
}
