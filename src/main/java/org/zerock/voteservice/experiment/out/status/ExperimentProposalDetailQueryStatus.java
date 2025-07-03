package org.zerock.voteservice.experiment.out.status;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExperimentProposalDetailQueryStatus implements ExperimentGrpcResponseStatus {
    OK(
            "OK",
            "요청 처리 성공",
            HttpStatus.OK // 200
    ),
    INVALID_PARAMETER(
      "INVALID_PARAMETER",
      "유효하지 않은 투표 제목입니다.",
            HttpStatus.BAD_REQUEST // 400
    ),
    PROPOSAL_NOT_FOUND(
            "PROPOSAL_NOT_FOUND",
            "해당 투표를 찾을 수 없습니다.",
            HttpStatus.BAD_REQUEST // 400
    ),
    BUSINESS_LOGIC_ERROR(
            "BUSINESS_LOGIC_ERROR",
            "비즈니스 로직 처리 중 오류가 발생했습니다.",
            HttpStatus.BAD_REQUEST // 400
    ),
    INTERNAL_SERVER_ERROR(
            "INTERNAL_SERVER_ERROR",
            "서버 내부에서 오류가 발생했습니다.",
            HttpStatus.INTERNAL_SERVER_ERROR // 500
    );

    private final String code;
    private final String message;
    private final HttpStatus httpStatusCode;

    ExperimentProposalDetailQueryStatus(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatus;
    }

    public static ExperimentProposalDetailQueryStatus fromCode(String code) {
        for (ExperimentProposalDetailQueryStatus status : ExperimentProposalDetailQueryStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }

        return INTERNAL_SERVER_ERROR;
    }

    public boolean isOk() {
        return this == OK;
    }
}
