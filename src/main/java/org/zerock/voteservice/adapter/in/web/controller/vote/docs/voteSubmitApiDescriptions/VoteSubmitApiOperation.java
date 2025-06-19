package org.zerock.voteservice.adapter.in.web.controller.vote.docs.voteSubmitApiDescriptions;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.zerock.voteservice.adapter.in.web.dto.vote.VoteSubmitRequestDto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Tag(name = "투표지 제출(생성)", description = "투표지 제출(생성) 관련 API")
@Operation(
        summary = "새로운 투표지 제출(생성)",
        description = """
                    새로운 투표지 제출(생성) 및 등록\n
                    - 투표지 등록 요청 interface: Schemas [ VoteSubmitRequestDto ] 참조\n
                    - 투표지 등록 응답 interface: Schemas [ VoteSubmitResponseDto ] 참조
                    """,
        requestBody = @RequestBody(
                description = "투표지 제출(생성) 위한 요청 Body",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = VoteSubmitRequestDto.class),
                        examples = @ExampleObject(
                                name = "투표지 제출 요청 예시",
                                summary = "법률 개정안 투표",
                                value = """
                                        {
                                          "hash": "YOUR_USER_HASH",
                                          "topic": "법률 개정안 찬반 투표",
                                          "option": "찬성"
                                        }"""
                        )
                )
        )
)
public @interface VoteSubmitApiOperation {
}
