package org.zerock.voteservice.adapter.in.web.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.zerock.voteservice.adapter.out.jpa.entity.UserEntity;
import org.zerock.voteservice.tool.hash.Sha256;


@Getter
@Builder
@AllArgsConstructor
public class UserRegisterServiceResult {
    private UserRegisterServiceStatus status;
    private UserEntity userEntity;
    private String userHash;

    public static UserRegisterServiceResult success(UserEntity userEntity) {
        return builder()
                .status(UserRegisterServiceStatus.OK)
                .userEntity(userEntity)
                .userHash(Sha256.sum(userEntity))
                .build();
    }

    public static UserRegisterServiceResult successWithoutData() {
        return builder()
                .status(UserRegisterServiceStatus.OK)
                .build();
    }

    public static UserRegisterServiceResult failure(UserRegisterServiceStatus status) {
        return builder()
                .status(status)
                .userEntity(null)
                .userHash(null)
                .build();
    }

    public Integer getUid() {
        return this.userEntity.getUid();
    }

    public String getUsername() {
        return this.userEntity.getUsername();
    }

    public boolean getSuccess() {
        return this.status.isOk();
    }
}
