package org.zerock.voteservice.security.user;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.zerock.voteservice.adapter.out.jpa.entity.UserEntity;
import org.zerock.voteservice.adapter.out.jpa.repository.UserRepository;

import java.util.Optional;

@Log4j2
@Service
@AllArgsConstructor
public class UserAuthenticationService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String logPrefix = String.format("[Username:%s] ", username);

        log.debug("{}Attempting to load user details", logPrefix);

        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        log.debug("{}User details successfully loaded [UID: {}]", logPrefix, userEntity.getUid());

        return new UserAuthenticationDetails(userEntity, null);
    }

    public Optional<UserEntity> loadUserByJwt(Long jwtUid, String jwtUsername) {
        return this.userRepository.findByUidAndUsername(jwtUid, jwtUsername);
    }
}
