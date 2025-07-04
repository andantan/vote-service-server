package org.zerock.voteservice.adapter.in.web.controller.docs.register.userRegisterApiDescriptions;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.zerock.voteservice.adapter.in.web.domain.dto.UserRegisterResponseDto;

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
                        신규 회원 등록 성공
                         - 신규 회원 등록 응답 interface: [ UserRegisterResponseDto ] 참조
                        """,
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = UserRegisterResponseDto.class),
                        examples = @ExampleObject(
                                name = "성공 응답 예시",
                                summary = "투표 등록 완료",
                                value = """
                                        {
                                          "success": true,
                                          "message": "신규 회원 검증 및 등록에 성공했습니다.",
                                          "grpcServerStatus": "OK",
                                          "http_status_code": 200,
                                          "uid": 156597113,
                                          "user_hash": "c947c3d95603d42a4c2c58f756c9d35dde729fcf85fa69ae0012dd59b06b6e83",
                                          "username": "user123"
                                        }"""
                        )
                )
        )
})
public @interface UserRegisterOkApiResponses {
}
