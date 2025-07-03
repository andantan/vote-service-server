package org.zerock.voteservice.experiment.in.domain.data;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.zerock.voteservice.experiment.out.status.ExperimentProposalDetailQueryStatus;
import org.zerock.voteservice.experiment.out.status.ExperimentExternalGrpcRuntimeStatus;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExperimentProposalDetailQueryResult<T extends ExperimentData>
        implements ExperimentResult {

    private ExperimentExternalGrpcRuntimeStatus experimentGrpcServerStatus;
    private ExperimentProposalDetailQueryStatus experimentGrpcResponseStatus;
    private T data;

    @Override
    public Boolean getSuccess() {
        return experimentGrpcServerStatus != null
                && experimentGrpcServerStatus.isOk()
                && experimentGrpcResponseStatus != null
                && experimentGrpcResponseStatus.isOk()
                && data != null;
    }

    @Override
    public String getStatus() {
        if (isGrpcServerError()) {
            return getGrpcServerStatusCode();
        }

        return getGrpcResponseStatusCode();
    }

    @Override
    public Integer getHttpStatusCode() {
        if (isGrpcServerError()) {
            return getGrpcServerStatusStatusCode().value();
        }

        return getGrpcResponseStatusStatusCode().value();
    }

    @Override
    public String getMessage() {
        if (isGrpcServerError()) {
            return getGrpcServerStatusMessage();
        }

        return getGrpcResponsetatusMessage();
    }

    @Override
    public String getGrpcServerStatusCode() {
        return experimentGrpcServerStatus.getCode();
    }

    @Override
    public String getGrpcServerStatusMessage() {
        return experimentGrpcServerStatus.getMessage();
    }

    @Override
    public HttpStatus getGrpcServerStatusStatusCode() {
        return experimentGrpcServerStatus.getHttpStatusCode();
    }

    @Override
    public String getGrpcResponseStatusCode() {
        return experimentGrpcResponseStatus.getCode();
    }

    @Override
    public String getGrpcResponsetatusMessage() {
        return experimentGrpcResponseStatus.getMessage();
    }

    @Override
    public HttpStatus getGrpcResponseStatusStatusCode() {
        return experimentGrpcResponseStatus.getHttpStatusCode();
    }

    @Override
    public Boolean isGrpcServerError() {
        return experimentGrpcServerStatus != null
                && !experimentGrpcServerStatus.isOk();
    }

    @Override
    public Boolean isGrpcResponseError() {
        return experimentGrpcResponseStatus != null
                && !experimentGrpcResponseStatus.isOk();
    }
}
