package org.zerock.voteservice.experiment.in.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.zerock.voteservice.security.user.UserAuthenticationDetails;

@Log4j2
@Component
public class ExperimentApiControllerHelper {
    public UserAuthenticationDetails getUserDetails() {
        log.debug("[ Experiment ] >>>>> Attempting get UserAuthenticationDetails");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthenticationDetails userDetails = (UserAuthenticationDetails) authentication.getPrincipal();

        log.debug("[ Experiment ] >>>>> User Authentication Details: {}", userDetails.getUsername());

        return userDetails;
    }
}
