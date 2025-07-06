package org.zerock.voteservice.adapter.out.grpc.stub;

import domain.event.pending.protocol.PendingExpiredEventResponse;
import io.grpc.Status;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.voteservice.BaseTestSettings;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class GrpcPendingEventServiceStubTest extends BaseTestSettings {

    @Autowired
    private GrpcPendingEventServiceStub stub;

    @Test
    void reportPendingExpiredEvent() {
        String topic = "test";
        int count = 1;
        Map<String, Integer> options = new HashMap<>();
        options.put("test", 2);

        try {
            PendingExpiredEventResponse response = stub.reportPendingExpiredEvent(topic, count, options);

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