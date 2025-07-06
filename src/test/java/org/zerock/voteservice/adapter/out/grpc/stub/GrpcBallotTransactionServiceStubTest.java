package org.zerock.voteservice.adapter.out.grpc.stub;

import domain.vote.submit.protocol.SubmitBallotTransactionResponse;
import io.grpc.Status;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.voteservice.BaseTestSettings;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class GrpcBallotTransactionServiceStubTest extends BaseTestSettings {

    @Autowired
    private GrpcBallotTransactionServiceStub stub;

    @Test
    void submitBallotTransaction() {
        String userHash = "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff";
        String topic = "test";
        String option = "1";

        try {
            SubmitBallotTransactionResponse response = stub.submitBallotTransaction(
                    userHash, topic, option
            );

            log.info("UserHash: {} -> Success: {}, Status: {}, VoteHash: {}",
                    userHash, response.getSuccess(), response.getStatus(), response.getVoteHash());

            assertNotNull(response);
        } catch (io.grpc.StatusRuntimeException e) {
            log.error(e.getMessage());

            if (e.getStatus().getCode() != Status.Code.UNAVAILABLE) {
                fail("Unexpected exception occurred during test: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Unexpected exception occurred during test: " + e.getMessage());
        }
    }
}