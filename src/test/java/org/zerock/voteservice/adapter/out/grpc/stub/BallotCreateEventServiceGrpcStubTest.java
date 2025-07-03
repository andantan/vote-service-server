package org.zerock.voteservice.adapter.out.grpc.stub;

import domain.event.ballot.create.protocol.BallotCacheEventResponse;
import domain.event.ballot.create.protocol.BallotValidateEventResponse;
import io.grpc.Status;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.zerock.voteservice.BaseTestSettings;
import org.zerock.voteservice.adapter.out.grpc.stub.common.exception.GrpcServiceUnavailableException;
import org.zerock.voteservice.tool.hash.Sha256;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class BallotCreateEventServiceGrpcStubTest extends BaseTestSettings {

    @Autowired
    private BallotCreateEventServiceGrpcStub stub;

    @Test
    void validateBallot() {
        String userHash = "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff";
        String topic = "test";
        String option = "2";

        try {
            BallotValidateEventResponse response = stub.validateBallot(userHash, topic, option);

            log.info(response);

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

    @Test
    void cacheBallot() {
        String userHash = "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff";
        String topic = "test";
        String voteHash = Sha256.sum(String.format("\"%s\"|\"%s\"|\"?\"", userHash, topic));

        try {
            BallotCacheEventResponse response = stub.cacheBallot(userHash, voteHash, topic);

            log.info(response);

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