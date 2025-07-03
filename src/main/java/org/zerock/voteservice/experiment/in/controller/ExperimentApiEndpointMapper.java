package org.zerock.voteservice.experiment.in.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(
        path = "${experiment.api.base-endpoint}",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public abstract class ExperimentApiEndpointMapper {

}
