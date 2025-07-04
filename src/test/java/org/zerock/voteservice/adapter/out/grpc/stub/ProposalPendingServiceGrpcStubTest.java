package org.zerock.voteservice.adapter.out.grpc.stub;

import domain.vote.proposal.protocol.OpenProposalPendingResponse;
import io.grpc.Status;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.voteservice.BaseTestSettings;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class ProposalPendingServiceGrpcStubTest extends BaseTestSettings {

    @Autowired
    private ProposalPendingServiceGrpcStub stub;

    @Test
    void openProposalPending() {
        String topic = "test";
        int duration = 2;

        try {
            OpenProposalPendingResponse response = stub.openProposalPending(topic, duration);

            log.info("Topic: {} -> Success: {}, Message: {}, Status: {}",
                    topic, response.getSuccess(), response.getMessage(), response.getStatus());

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