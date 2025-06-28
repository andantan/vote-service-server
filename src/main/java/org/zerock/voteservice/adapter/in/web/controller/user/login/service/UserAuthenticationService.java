package org.zerock.voteservice.adapter.in.web.controller.user.login.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.zerock.voteservice.adapter.in.web.dto.user.authentication.UserAuthenticationDetails;

import org.zerock.voteservice.adapter.out.persistence.entity.UserEntity;
import org.zerock.voteservice.adapter.out.persistence.repository.UserRepository;

@Log4j2
@Service
public class UserAuthenticationService implements UserDetailsService {
    private final UserRepository userRepository;

    public UserAuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username);

        if (userEntity == null) {
            throw new UsernameNotFoundException(username);
        }

        return new UserAuthenticationDetails(userEntity, null);
    }
}
