package org.zerock.voteservice.experiment.out.proxy;

import domain.event.proposal.query.protocol.GetProposalDetailResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.zerock.voteservice.experiment.common.ExperimentGrpcExceptionHandler;
import org.zerock.voteservice.experiment.in.domain.data.ExperimentProposalDetailQueryGrpcResponseData;
import org.zerock.voteservice.experiment.in.domain.data.ExperimentProposalDetailQueryGrpcResult;
import org.zerock.voteservice.experiment.in.domain.dto.ExperimentProposalDetailQueryRequestDto;
import org.zerock.voteservice.experiment.out.status.ExperimentExternalGrpcRuntimeStatus;
import org.zerock.voteservice.experiment.out.status.ExperimentProposalDetailQueryStatus;
import org.zerock.voteservice.experiment.out.stub.ExperimentProposalQueryEventServiceGrpcStub;

@Log4j2
@Component
public class ExperimentProposalQueryProxy {
    private final ExperimentProposalQueryEventServiceGrpcStub stub;

    public ExperimentProposalQueryProxy(ExperimentProposalQueryEventServiceGrpcStub stub) {
        this.stub = stub;
    }

    public ExperimentProposalDetailQueryGrpcResult getProposalDetail(
            ExperimentProposalDetailQueryRequestDto dto
    ) {
        ExperimentProposalDetailQueryGrpcResult result
                = new ExperimentProposalDetailQueryGrpcResult();

        GetProposalDetailResponse response;
        ExperimentExternalGrpcRuntimeStatus serverStatus;
        ExperimentProposalDetailQueryStatus serviceStatus;
        ExperimentProposalDetailQueryGrpcResponseData data;

        try {
            response = this.stub.getProposalDetail(dto.getTopic());

            serverStatus = ExperimentExternalGrpcRuntimeStatus.OK;
            serviceStatus = ExperimentProposalDetailQueryStatus.fromCode(response.getStatus());
            data = new ExperimentProposalDetailQueryGrpcResponseData(response);

        } catch (io.grpc.StatusRuntimeException e) {
            serverStatus = ExperimentGrpcExceptionHandler.mapStatusRuntimeExceptionToExternalStatus(e);
            serviceStatus = ExperimentProposalDetailQueryStatus.BUSINESS_LOGIC_ERROR;
            data = null;

            String errorLogMessage = String.format("%sgRPC call failed due to %s server unavailability or host resolution issue: [Status: %s, Description: \"%s\"]",
                    stub.getLogPrefix(), stub.getLayerName(), e.getStatus().getCode(), e.getStatus().getDescription());

            log.error(errorLogMessage);

        } catch (Exception e) {
            serverStatus = ExperimentExternalGrpcRuntimeStatus.INTERNAL_SERVER_ERROR;
            serviceStatus = ExperimentProposalDetailQueryStatus.BUSINESS_LOGIC_ERROR;
            data = null;
        }

        result.setExperimentGrpcServerStatus(serverStatus);
        result.setExperimentGrpcResponseStatus(serviceStatus);
        result.setExperimentGrpcResponseData(data);

        return result;
    }
}
