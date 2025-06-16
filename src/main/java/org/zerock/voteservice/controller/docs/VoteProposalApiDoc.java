package org.zerock.voteservice.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;

import org.zerock.voteservice.controller.docs.proposalApiResponses.ProposalBadRequestApiResponses;
import org.zerock.voteservice.controller.docs.proposalApiResponses.ProposalConflictApiResponses;
import org.zerock.voteservice.controller.docs.proposalApiResponses.ProposalInternalServerErrorApiResponses;
import org.zerock.voteservice.dto.vote.VoteProposalRequestDto;
import org.zerock.voteservice.dto.vote.VoteProposalResponseDto;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Tag(name = "투표 관리", description = "투표 생성 관련 API")
@Operation(
        summary = "새로운 투표 제안 생성",
        description = """
                    새로운 투표 제안 및 등록\n
                    - 투표 생성 요청 interface: Schemas [ VoteProposalRequestDto ] 참조\n
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
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "투표 제안 성공",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = VoteProposalResponseDto.class),
                        examples = @ExampleObject(
                                name = "성공 응답 예시",
                                summary = "투표 등록 완료",
                                value = """
                                        {
                                          "success": true,
                                          "topic": "법률 개정안 찬반 투표",
                                          "duration": 60,
                                          "message": "투표 등록이 완료되었습니다.",
                                          "status": "OK",
                                          "http_status_code": 200
                                        }"""
                        )
                )
        )
})
@ProposalBadRequestApiResponses
@ProposalConflictApiResponses
@ProposalInternalServerErrorApiResponses
public @interface VoteProposalApiDoc { }