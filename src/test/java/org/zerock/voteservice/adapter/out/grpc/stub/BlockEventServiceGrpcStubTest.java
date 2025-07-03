package org.zerock.voteservice.adapter.out.grpc.stub;

import domain.event.block.protocol.BlockCreatedEventResponse;
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
class BlockEventServiceGrpcStubTest extends BaseTestSettings {

    @Autowired
    private BlockEventServiceGrpcStub stub;

    @Test
    void reportBlockCreatedEvent() {
        String topic = "개돼지 투표";
        int txCount = 0;
        int height = 999999999;

        try {
            BlockCreatedEventResponse response = stub.reportBlockCreatedEvent(
                    topic, txCount, height
            );

            log.info("Topic: {} -> Cached: {}, Status: {}",
                    topic, response.getCached(), response.getStatus());

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