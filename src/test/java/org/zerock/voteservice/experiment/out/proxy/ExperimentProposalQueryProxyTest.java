package org.zerock.voteservice.experiment.out.proxy;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.voteservice.BaseTestSettings;
import org.zerock.voteservice.experiment.in.domain.data.ExperimentProposalDetailQueryGrpcResult;
import org.zerock.voteservice.experiment.in.domain.dto.ExperimentProposalDetailQueryRequestDto;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class ExperimentProposalQueryProxyTest extends BaseTestSettings {

    @Autowired
    private ExperimentProposalQueryProxy proxy;

    @Test
    void getProposalDetail() {
        String topic = "개돼지 투표";

        ExperimentProposalDetailQueryRequestDto dto = ExperimentProposalDetailQueryRequestDto.builder().topic(topic).build();

        ExperimentProposalDetailQueryGrpcResult result = proxy.getProposalDetail(dto);

        assertNotNull(result);

        if (result.getSuccess()) {
            assertNotNull(result.getExperimentGrpcResponseData());
        } else {
            assertNull(result.getExperimentGrpcResponseData());
        }

        log.info(result.getSuccess());
        log.info(result.getStatus());
        log.info(result.getMessage());
        log.info(result.getHttpStatusCode());
    }
}