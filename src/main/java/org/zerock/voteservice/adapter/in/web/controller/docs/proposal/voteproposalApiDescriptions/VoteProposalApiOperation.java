package org.zerock.voteservice.adapter.in.web.controller.docs.proposal.voteproposalApiDescriptions;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.zerock.voteservice.adapter.in.web.domain.dto.VoteProposalRequestDto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Tag(name = "투표 생성", description = "투표 생성 관련 API")
@Operation(
        summary = "새로운 투표 제안 생성",
        description = """
                    새로운 투표 제안 및 등록
                    
                    - 투표 생성 요청 interface: Schemas [ VoteProposalRequestDto ] 참조
                    
                    - 투표 생성 응답 interface: Schemas [ VoteProposalResponseDto ] 참조
                    """,
        requestBody = @RequestBody(
                description = "새로운 투표 제안을 위한 요청 Body",
                required = true,
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = VoteProposalRequestDto.class),
                        examples = @ExampleObject(
                                name = "투표 제안 요청 예시",
                                summary = "새로운 투표 생성",
                                value = """
                                    {
                                      "topic": "법률 개정안 찬반 투표",
                                      "duration": 24,
                                      "options": ["찬성", "반대", "기권"]
                                    }"""
                        )
                )
        )
)
public @interface VoteProposalApiOperation {
}
