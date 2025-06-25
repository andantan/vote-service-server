package org.zerock.voteservice.adapter.in.web.controller.user.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.zerock.voteservice.adapter.in.web.dto.user.authentication.UserAuthenticationDetails;

import org.zerock.voteservice.adapter.out.persistence.entity.UserEntity;
import org.zerock.voteservice.adapter.out.persistence.repository.UserRepository;

@Service
public class UserAuthenticationService implements UserDetailsService {
    private final UserRepository userRepository;

    public UserAuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username);

        if (userEntity != null) {
            return new UserAuthenticationDetails(userEntity);
        }

        return null;
    }
}
