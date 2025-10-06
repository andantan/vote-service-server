package org.zerock.voteservice.adapter.out.jpa.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.zerock.voteservice.adapter.out.jpa.repository.UserVerificationRepository;

import java.util.Date;

@Log4j2
@Component
@RequiredArgsConstructor
public class VerificationCleanupScheduler {

    private final UserVerificationRepository userVerificationRepository;

    @Scheduled(cron = "0 */10 * * * *")
    public void cleanupExpiredVerification() {
        userVerificationRepository.deleteByExpirationBefore(new Date());
    }
}
