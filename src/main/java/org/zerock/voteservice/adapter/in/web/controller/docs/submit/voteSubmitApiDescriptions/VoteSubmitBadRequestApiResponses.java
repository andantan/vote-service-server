package org.zerock.voteservice.adapter.in.web.controller.docs.submit.voteSubmitApiDescriptions;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.zerock.voteservice.adapter.in.web.dto.vote.error.VoteErrorResponseDto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "400",
                description = "잘못된 요청 데이터 (예: 유효하지 않은 옵션, 해시 길이/해독 오류, 만료된 투표 등)",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = VoteErrorResponseDto.class),
                        examples = {
                                @ExampleObject(
                                        name = "진행이 완료된 투표 접근",
                                        summary = "PROPOSAL_EXPIRED 오류",
                                        value = """
                                                {
                                                  "success": false,
                                                  "message": "이미 진행이 완료된 투표입니다.",
                                                  "status": "PROPOSAL_EXPIRED",
                                                  "http_status_code": 400
                                                }"""
                                ),
                                @ExampleObject(
                                        name = "유효하지 않은 투표 선택 사항",
                                        summary = "INVALID_OPTION 오류",
                                        value = """
                                                {
                                                  "success": false,
                                                  "message": "비정상적인 투표 선택 사항입니다.",
                                                  "status": "INVALID_OPTION",
                                                  "http_status_code": 400
                                                }"""
                                ),
                                @ExampleObject(
                                        name = "비정상적인 해시값 (길이 오류)",
                                        summary = "INVALID_HASH_LENGTH 오류",
                                        value = """
                                                {
                                                  "success": false,
                                                  "message": "비정상적인 해시값입니다. (해시 길이 오류)",
                                                  "status": "INVALID_HASH_LENGTH",
                                                  "http_status_code": 400
                                                }"""
                                ),
                                @ExampleObject(
                                        name = "비정상적인 해시값 (해독 오류)",
                                        summary = "DECODE_ERROR 오류",
                                        value = """
                                                {
                                                  "success": false,
                                                  "message": "비정상적인 해시값입니다. (해시 해독 오류)",
                                                  "status": "DECODE_ERROR",
                                                  "http_status_code": 400
                                                }"""
                                )
                        }
                )
        )
})
public @interface VoteSubmitBadRequestApiResponses { }