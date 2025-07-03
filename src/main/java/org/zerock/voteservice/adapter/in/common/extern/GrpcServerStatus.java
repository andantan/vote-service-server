package org.zerock.voteservice.adapter.in.common.extern;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum GrpcServerStatus {
    OK(
            "OK",
            "%s 서버와의 통신에 성공했습니다.",
            HttpStatus.OK
    ),
    INTERNAL_SERVER_ERROR(
            "INTERNAL_SERVER_ERROR",
            "%s 서버 내부에서 오류가 발생했습니다.",
            HttpStatus.INTERNAL_SERVER_ERROR // 500
    ),
    BAD_GATEWAY(
            "BAD_GATEWAY",
            "%s 서버와의 통신 중 문제가 발생했습니다.",
            HttpStatus.BAD_GATEWAY // 502
    ),
    SERVICE_UNAVAILABLE(
            "SERVICE_UNAVAILABLE",
            "%s 서버와의 통신이 불가능합니다.",
            HttpStatus.SERVICE_UNAVAILABLE // 503
    );

    private final String code;
    private final String messageTemplate;
    private final HttpStatus httpStatusCode;

    GrpcServerStatus(String code, String messageTemplate, HttpStatus httpStatusCode) {
        this.code = code;
        this.messageTemplate = messageTemplate;
        this.httpStatusCode = httpStatusCode;
    }

    public static GrpcServerStatus fromCode(String code) {
        for (GrpcServerStatus grpcServerStatus : GrpcServerStatus.values()) {
            if (grpcServerStatus.code.equals(code)) {
                return grpcServerStatus;
            }
        }

        return BAD_GATEWAY;
    }

    public String getFormattedMessageByLayerCode(Layer layer) {
        return String.format(this.messageTemplate, layer.getLayerCode());
    }

    public String getFormattedMessageByLayerName(Layer layer) {
        return String.format(this.messageTemplate, layer.getLayerName());
    }

    public String getFormattedMessageByFullName(Layer layer) {
        return String.format(this.messageTemplate, layer.getFullName());
    }
}
