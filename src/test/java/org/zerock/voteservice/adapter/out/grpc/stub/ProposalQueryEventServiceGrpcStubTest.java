package org.zerock.voteservice.adapter.out.grpc.stub;

import domain.event.proposal.query.protocol.*;
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
class ProposalQueryEventServiceGrpcStubTest extends BaseTestSettings {

    @Autowired
    private ProposalQueryEventServiceGrpcStub stub;

    @Test
    void getProposalDetail() {
        String topic = "test";

        try {
            GetProposalDetailResponse response = stub.getProposalDetail(topic);

            log.info(response);

            assertNotNull(response);
        }  catch (GrpcServiceUnavailableException e) {
            log.error(e.getMessage());

            if (e.getStatus().getCode() != Status.Code.UNAVAILABLE) {
                fail("Unexpected exception occurred during test: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Unexpected exception occurred during test: " + e.getMessage());
        }
    }

    @Test
    void getFilteredProposalList() {
        Filter filter = Filter.newBuilder().setExpired(true).build();
        Sort sort = Sort.newBuilder().setSortOrder("desc").setSortBy("expiredAt").build();
        Paging paging = Paging.newBuilder().setSkip(0).setLimit(2).build();

        try {
            GetFilteredProposalListResponse response = stub.getFilteredProposalList(filter, sort, paging);

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