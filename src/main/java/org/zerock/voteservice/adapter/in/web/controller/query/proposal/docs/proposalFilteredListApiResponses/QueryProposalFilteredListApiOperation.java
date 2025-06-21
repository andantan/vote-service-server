package org.zerock.voteservice.adapter.in.web.controller.query.proposal.docs.proposalFilteredListApiResponses;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Tag(name = "투표 목록 조회", description = "투표 조회 관련 API")
@Operation(
        summary = "투표 목록 조회",
        description = """
                    필터링 옵션으로 투표 목록 조회
                    
                    - 투표 목록 조회 요청 interface: Schemas 없음 [ Query parameter에 따름 ]
                    
                    - 투표 목록 조회 응답 interface: Schemas [ QueryProposalFilteredListResponseDto ] 참조
                    """,
        parameters = {
                @Parameter(
                        name = "summarize",
                        in = ParameterIn.QUERY,
                        description = "요약 정보만 포함할지 여부",
                        example = "true",
                        required = true,

                        schema = @Schema(type = "boolean", defaultValue = "false")
                ),
                @Parameter(
                        name = "expired",
                        in = ParameterIn.QUERY,
                        description = "만료된 투표 포함 여부 (생략 시 전체 포함)",
                        example = "true",
                        schema = @Schema(type = "boolean", nullable = true)
                ),
                @Parameter(
                        name = "page",
                        in = ParameterIn.QUERY,
                        description = "조회할 페이지 번호 (1부터 시작)",
                        example = "1",
                        required = true,
                        schema = @Schema(type = "integer", format = "int32", defaultValue = "1")
                ),
                @Parameter(
                        name = "limit",
                        in = ParameterIn.QUERY,
                        description = "한 페이지당 조회할 투표 개수",
                        example = "15",
                        required = true,
                        schema = @Schema(type = "integer", format = "int32", defaultValue = "15")
                )
        }
)
public @interface QueryProposalFilteredListApiOperation {
}
