package org.zerock.voteservice.adapter.in.web.controller.docs.proposal.proposalDetailQueryApiResponses;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Tag(name = "투표 상세 내역 조회", description = "투표 조회 관련 API")
@Operation(
        summary = "주제(topic)로 투표 조회",
        description = """
                    고유한 투표 주제(이름)를 기준으로 특정 투표 정보를 조회
                    
                    - 투표 조회 요청 interface: Schemas [ QueryProposalDetailRequestDto ] 참조
                    
                    - 투표 조회 응답 interface: Schemas [ QueryProposalDetailResponseDto ] 참조
                    """,
        parameters = {
                @Parameter(
                        name = "topicName",
                        in = ParameterIn.PATH,
                        description = "조회할 투표의 고유 주제(이름)",
                        example = "기본 소득 제도 도입 찬반",
                        required = true,
                        schema = @Schema(type = "string")
                )
        }
)
public @interface QueryProposalDetailApiOperation {
}
