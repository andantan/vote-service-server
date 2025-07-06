package org.zerock.voteservice.adapter.out.grpc.stub;

import domain.event.proposal.create.protocol.ProposalCacheEventResponse;
import domain.event.proposal.create.protocol.ProposalValidateEventResponse;
import io.grpc.Status;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.voteservice.BaseTestSettings;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class GrpcProposalCreateEventServiceStubTest extends BaseTestSettings {

    @Autowired
    private GrpcProposalCreateEventServiceStub stub;

    @Test
    void validateProposal() {
        String topic = "test";

        try {
            ProposalValidateEventResponse response = stub.validateProposal(topic);

            log.info("Topic: {} -> Validation: {}, Status: {}",
                    topic, response.getValidation(), response.getStatus());

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

    @Test
    void cacheProposal() {
        String topic = "test";
        int duration = 1;
        List<String> options = List.of("1", "2", "3");

        try {
            ProposalCacheEventResponse response = stub.cacheProposal(topic, duration, options);

            log.info("Topic: {} -> Cached: {}, Status: {}",
                    topic, response.getCached(), response.getStatus());

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