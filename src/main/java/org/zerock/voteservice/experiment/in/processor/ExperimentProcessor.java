package org.zerock.voteservice.experiment.in.processor;

import org.springframework.http.ResponseEntity;
import org.zerock.voteservice.experiment.in.domain.data.ExperimentResult;
import org.zerock.voteservice.experiment.in.domain.dto.ExperimentRequestDto;
import org.zerock.voteservice.experiment.in.domain.dto.ExperimentResponseDto;

public interface ExperimentProcessor<
        T extends ExperimentRequestDto,
        V extends ExperimentResult
        > {

    ResponseEntity<? extends ExperimentResponseDto> getSuccessResponseEntity(T dto, V result);
    ResponseEntity<? extends ExperimentResponseDto> getfailureResponseEntity(V result);
}
