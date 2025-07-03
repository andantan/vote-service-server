package org.zerock.voteservice.adapter.in.web.controller.docs.submit.voteSubmitApiDescriptions;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.zerock.voteservice.adapter.in.web.dto.VoteSubmitResponseDto;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = """
                        투표지 생성 성공
                         - 투표지 생성 응답 interface: [ VoteSubmitResponseDto ] 참조
                        """,
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = VoteSubmitResponseDto.class),
                        examples = @ExampleObject(
                                name = "성공 응답 예시",
                                summary = "투표지 등록 완료",
                                value = """
                                        {
                                          "success": true,
                                          "topic": "법률 개정안 찬반 투표",
                                          "message": "투표 참여가 완료되었습니다.",
                                          "grpcServerStatus": "OK",
                                          "http_status_code": 200,
                                          "user_hash": "1303522e0b59179b948c1713105ac8f9d13c62080a743118617fecef799e92e5",
                                          "vote_option": "찬성",
                                          "vote_hash": "aeb4b82ecad02ae8f76762ebeaf5335a121f25501ba0302c77367b663b70e8b3"
                                        }"""
                        )
                )
        )
})
public @interface VoteSubmitOkApiResponses {
}
