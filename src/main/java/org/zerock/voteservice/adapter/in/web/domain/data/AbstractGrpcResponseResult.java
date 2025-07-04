package org.zerock.voteservice.adapter.in.web.domain.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import org.springframework.http.HttpStatus;

import org.zerock.voteservice.adapter.out.grpc.status.GrpcResponseStatus;
import org.zerock.voteservice.adapter.out.grpc.status.GrpcRuntimeStatus;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractGrpcResponseResult<S extends GrpcResponseStatus, D extends GrpcResponseData>
    implements GrpcResponseResult {
    private GrpcRuntimeStatus GrpcServerStatus;
    private S GrpcResponseStatus;
    private D GrpcResponseData;

    @Override
    public Boolean getSuccess() {
        return GrpcServerStatus != null
                && GrpcServerStatus.isOk()
                && GrpcResponseStatus != null
                && GrpcResponseStatus.isOk()
                && GrpcResponseData != null;
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
        return GrpcServerStatus.getCode();
    }

    @Override
    public String getGrpcServerStatusMessage() {
        return GrpcServerStatus.getMessage();
    }

    @Override
    public HttpStatus getGrpcServerStatusStatusCode() {
        return GrpcServerStatus.getHttpStatusCode();
    }

    @Override
    public String getGrpcResponseStatusCode() {
        return GrpcResponseStatus.getCode();
    }

    @Override
    public String getGrpcResponsetatusMessage() {
        return GrpcResponseStatus.getMessage();
    }

    @Override
    public HttpStatus getGrpcResponseStatusStatusCode() {
        return GrpcResponseStatus.getHttpStatusCode();
    }

    @Override
    public Boolean isGrpcServerError() {
        return GrpcServerStatus != null
                && !GrpcServerStatus.isOk();
    }

    @Override
    public Boolean isGrpcResponseError() {
        return GrpcResponseStatus != null
                && !GrpcResponseStatus.isOk();
    }
}
