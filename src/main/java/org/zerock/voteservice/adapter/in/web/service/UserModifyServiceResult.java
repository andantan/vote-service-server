package org.zerock.voteservice.adapter.in.web.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.zerock.voteservice.adapter.out.jpa.entity.UserEntity;
import org.zerock.voteservice.tool.hash.Sha256;

@Getter
@Builder
@AllArgsConstructor
public class UserModifyServiceResult {
    private UserModifyServiceStatus status;
    private UserEntity userEntity;

    @Getter
    private String userhash;

    public static UserModifyServiceResult success(UserEntity userEntity) {
        return builder()
                .status(UserModifyServiceStatus.OK)
                .userEntity(userEntity)
                .userhash(Sha256.sum(userEntity))
                .build();
    }

    public static UserModifyServiceResult failure(UserModifyServiceStatus status) {
        return builder()
                .status(status)
                .userEntity(null)
                .userhash(null)
                .build();
    }

    public Long getUid() {
        return this.userEntity.getUid();
    }

    public boolean getSuccess() {
        return this.status.isOk();
    }
}
