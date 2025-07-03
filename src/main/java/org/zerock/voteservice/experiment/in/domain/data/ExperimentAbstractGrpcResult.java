package org.zerock.voteservice.experiment.in.domain.data;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;
import org.zerock.voteservice.experiment.out.status.ExperimentGrpcResponseStatus;
import org.zerock.voteservice.experiment.out.status.ExperimentExternalGrpcRuntimeStatus;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class ExperimentAbstractGrpcResult<T extends ExperimentGrpcResponseStatus, S extends ExperimentGrpcResponseData>
        implements ExperimentGrpcResult {

    private ExperimentExternalGrpcRuntimeStatus experimentGrpcServerStatus;
    private T experimentGrpcResponseStatus;
    private S experimentGrpcResponseData;

    @Override
    public Boolean getSuccess() {
        return experimentGrpcServerStatus != null
                && experimentGrpcServerStatus.isOk()
                && experimentGrpcResponseStatus != null
                && experimentGrpcResponseStatus.isOk()
                && experimentGrpcResponseData != null;
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
