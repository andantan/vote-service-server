package org.zerock.voteservice.adapter.in.web.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.zerock.voteservice.adapter.out.jpa.entity.UserVerificationEntity;

import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
public class UserVerificationResult {
    private UserVerificationStatus status;
    private UserVerificationEntity entity;

    public static UserVerificationResult success(UserVerificationEntity entity) {
        return builder()
                .status(UserVerificationStatus.OK)
                .entity(entity)
                .build();
    }

    public static UserVerificationResult failure(UserVerificationStatus status) {
        return builder()
                .status(status)
                .entity(null)
                .build();
    }

    public String getUsername() {
        return this.entity.getUsername();
    }

    public String getEmail() {
        return this.entity.getEmail();
    }

    public Date getExpiration() {
        return this.entity.getExpiration();
    }

    public Long getUid() {
        return this.entity.getUid();
    }

    public boolean getSuccess() {
        return this.status.isOk();
    }
}
