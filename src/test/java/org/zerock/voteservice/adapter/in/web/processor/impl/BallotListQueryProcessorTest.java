package org.zerock.voteservice.adapter.in.web.processor.impl;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.voteservice.BaseTestSettings;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcBallotListQueryResponseResult;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.BallotListQueryRequestDto;
import org.zerock.voteservice.adapter.out.grpc.proxy.BallotQueryProxy;

@SpringBootTest
@Log4j2
class BallotListQueryProcessorTest extends BaseTestSettings {

    @Autowired
    BallotQueryProxy proxy;

    @Test
    void execute() {
        String userHash = "f786c22ff8faf35a0ece645064e48ad2d36c4beacfb19a4c1ae1dbb7da753a";

        BallotListQueryRequestDto dto = BallotListQueryRequestDto.builder().userHash(userHash).build();

        GrpcBallotListQueryResponseResult result = proxy.getBallotList(dto);

        log.info("UserHash: {} -> Success: {}, Status: {}, Message: {}, HttpStatusCode: {}",
                userHash, result.getSuccess(), result.getStatus(), result.getMessage(), result.getHttpStatusCode());

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