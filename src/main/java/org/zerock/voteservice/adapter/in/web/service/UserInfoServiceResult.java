package org.zerock.voteservice.adapter.in.web.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.zerock.voteservice.adapter.out.jpa.entity.UserEntity;
import org.zerock.voteservice.tool.hash.Sha256;

@Getter
@Builder
@AllArgsConstructor
public class UserInfoServiceResult {
    private UserInfoServiceStatus status;
    private UserEntity userEntity;

    @Getter
    private String userHash;

    public static UserInfoServiceResult success(UserEntity userEntity) {
        return builder()
                .status(UserInfoServiceStatus.OK)
                .userEntity(userEntity)
                .userHash(Sha256.sum(userEntity))
                .build();
    }

    public static UserInfoServiceResult failure(UserInfoServiceStatus status) {
        return builder()
                .status(status)
                .userEntity(null)
                .userHash(null)
                .build();
    }

    public Long getUid() {
        return this.userEntity.getUid();
    }

    public String getUsername() {
        return this.userEntity.getUsername();
    }

    public String getRealName() {
        return this.userEntity.getRealName();
    }

    public String getEmail() {
        return this.userEntity.getEmail();
    }

    public String getPhoneNumber() {
        return this.userEntity.getPhoneNumber();
    }

    public boolean getSuccess() {
        return this.status.isOk();
    }
}
