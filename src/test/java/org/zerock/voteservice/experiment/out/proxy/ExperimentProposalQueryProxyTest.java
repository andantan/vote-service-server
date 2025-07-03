package org.zerock.voteservice.experiment.out.proxy;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.voteservice.BaseTestSettings;
import org.zerock.voteservice.experiment.in.domain.data.ExperimentProposalDetailQueryData;
import org.zerock.voteservice.experiment.in.domain.data.ExperimentProposalDetailQueryResult;
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

        ExperimentProposalDetailQueryResult<ExperimentProposalDetailQueryData> result = proxy.getProposalDetail(dto);

        assertNotNull(result);

        if (result.getSuccess()) {
            assertNotNull(result.getData());
        } else {
            assertNull(result.getData());
        }

        log.info(result.getMessage());
    }
}