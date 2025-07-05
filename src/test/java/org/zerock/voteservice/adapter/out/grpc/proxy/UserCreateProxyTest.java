package org.zerock.voteservice.adapter.out.grpc.proxy;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.voteservice.BaseTestSettings;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.UserValidationRequestDto;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcUserValidationResponseResult;

@SpringBootTest
@Log4j2
class UserCreateProxyTest extends BaseTestSettings {

    @Autowired
    private UserCreateProxy proxy;

    @Test
    void validateUser() {
        Integer uid = 5;
        String userHash = "0fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff";

        UserValidationRequestDto dto = UserValidationRequestDto.builder()
                .uid(uid)
                .userHash(userHash)
                .build();

        GrpcUserValidationResponseResult result = proxy.validateUser(dto);

        log.info("Uid: {}, UserHash: {} -> Success: {}, Status: {}, Message: {}, HttpStatusCode: {}",
                uid, userHash, result.getSuccess(), result.getStatus(), result.getMessage(), result.getHttpStatusCode());

        log.info("Server -> Status: {}, Message: {}, HttpStatusCode: {}",
                result.getGrpcServerStatus().getCode(),
                result.getGrpcServerStatus().getMessage(),
                result.getGrpcServerStatus().getHttpStatusCode()
        );

        log.info("Response -> Status: {}, Message: {}, HttpStatusCode: {}",
                result.getGrpcResponseStatus().getCode(),
                result.getGrpcResponseStatus().getMessage(),
                result.getGrpcResponseStatus().getHttpStatusCode()
        );
    }
}