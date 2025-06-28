package org.zerock.voteservice.adapter.out.grpc.proxy.user;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import domain.event.user.create.protocol.UserValidateEventResponse;
import domain.event.user.create.protocol.UserCacheEventResponse;

import org.zerock.voteservice.adapter.in.web.dto.user.register.UserCacheRequestDto;
import org.zerock.voteservice.adapter.out.grpc.stub.mongodbServer.userData.UserCreateEventServiceGrpcStub;

@Log4j2
@Service
public class UserCreateProxy {
    private final UserCreateEventServiceGrpcStub userCreateEventServiceGrpcStub;

    public UserCreateProxy(
            UserCreateEventServiceGrpcStub userCreateEventServiceGrpcStub
    ) {
        this.userCreateEventServiceGrpcStub = userCreateEventServiceGrpcStub;
    }

    public UserValidateEventResponse validateUser(UserCacheRequestDto dto) {
        return this.userCreateEventServiceGrpcStub.validateUser(
                dto.getUid(), dto.getUserHash()
        );
    }

    public UserCacheEventResponse cacheUser(UserCacheRequestDto dto) {
        return this.userCreateEventServiceGrpcStub.cacheUser(
                dto.getUid(), dto.getUserHash(), dto.getGender(), dto.getBirthDate()
        );
    }
}
