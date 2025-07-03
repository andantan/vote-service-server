package org.zerock.voteservice.adapter.in.web.processor;

import domain.event.ballot.query.protocol.GetUserBallotsResponse;
import io.grpc.Status;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.voteservice.BaseTestSettings;
import org.zerock.voteservice.adapter.in.common.ProcessorResult;
import org.zerock.voteservice.adapter.in.common.extern.GrpcServerStatus;
import org.zerock.voteservice.adapter.in.common.extern.GrpcStatus;
import org.zerock.voteservice.adapter.in.web.dto.QueryBallotRequestDto;
import org.zerock.voteservice.adapter.out.grpc.proxy.BallotQueryProxy;
import org.zerock.voteservice.adapter.out.grpc.stub.common.exception.GrpcServiceUnavailableException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class BallotQueryProcessorTest extends BaseTestSettings {

    @Autowired
    private BallotQueryProxy proxy;

    @Test
    void validateUserHash() {
        String userHash = "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff";

        QueryBallotRequestDto dto = QueryBallotRequestDto.builder().userHash(userHash).build();

        try {
            GetUserBallotsResponse response = proxy.getUserBallots(dto);

            BallotQueryProcessorData data = BallotQueryProcessorData.builder()
                    .ballotList(response.getBallotsList())
                    .build();

            GrpcStatus grpcStatus = GrpcStatus.ofMongoDB(
                    GrpcServerStatus.fromCode(response.getStatus())
            );

            ProcessorResult<BallotQueryProcessorData> processorResult = ProcessorResult.success(grpcStatus, data);

            log.info(processorResult.getGrpcStatus().getMessageByFullName());
            log.info(processorResult.getGrpcStatus().isSuccess());
            log.info(processorResult.getData().getBallotList());

        } catch (GrpcServiceUnavailableException e) {
            log.error(e.getMessage());

            if (e.getStatus().getCode() != Status.Code.UNAVAILABLE) {
                fail("Unexpected exception occurred during test: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Unexpected exception occurred during test: " + e.getMessage());
        }

    }

    @Test
    void processBallotQuery() {
    }

    @Test
    void getSuccessResponse() {
    }

    @Test
    void getErrorResponse() {
    }

    @Test
    void testGetErrorResponse() {
    }
}