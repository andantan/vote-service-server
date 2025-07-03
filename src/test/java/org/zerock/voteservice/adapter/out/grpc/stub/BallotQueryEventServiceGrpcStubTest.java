package org.zerock.voteservice.adapter.out.grpc.stub;

import domain.event.ballot.query.protocol.GetUserBallotsResponse;
import io.grpc.Status;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.voteservice.BaseTestSettings;
import org.zerock.voteservice.adapter.out.grpc.stub.common.exception.GrpcServiceUnavailableException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class BallotQueryEventServiceGrpcStubTest extends BaseTestSettings {

    @Autowired
    private BallotQueryEventServiceGrpcStub stub;

    @Test
    void getUserBallots() {
        String userHash = "f786c22ff8faf35a0ece645064e48ad2d36c4beacfb19a4c1ae1dbb7da753a2f";

        try {
            GetUserBallotsResponse response = stub.getUserBallots(userHash);

            log.info("UserHash: {} -> Quried: {}, Status: {}",
                    userHash, response.getQueried(), response.getStatus());

            response.getBallotsList().forEach(ballot ->
                    log.info("\t -> Ballot: {}", ballot)
            );

            assertNotNull(response);
        } catch (GrpcServiceUnavailableException e) {
            log.error(e.getMessage());

            if (e.getStatus().getCode() != Status.Code.UNAVAILABLE) {
                fail("Unexpected exception occurred during test: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Unexpected exception occurred during test: " + e.getMessage());
        }
    }
}