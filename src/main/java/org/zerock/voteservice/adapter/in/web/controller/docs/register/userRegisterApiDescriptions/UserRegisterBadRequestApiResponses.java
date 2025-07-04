package org.zerock.voteservice.adapter.in.web.controller.docs.register.userRegisterApiDescriptions;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.zerock.voteservice.adapter.in.web.domain.dto.user.error.UserErrorResponseDto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "400",
                description = "신규 회원 등록 실패 (잘못된 요청 데이터 또는 존재하는 회원 정보)",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = UserErrorResponseDto.class),
                        examples = {
                                @ExampleObject(
                                        name = "존재하는 로그인 아이디",
                                        summary = "EXIST_USERNAME 오류",
                                        value = """
                                                {
                                                  "success": false,
                                                  "message": "존재하는 로그인 아이디입니다.",
                                                  "grpcServerStatus": "EXIST_USERNAME",
                                                  "http_status_code": 400
                                                }"""
                                ),
                                @ExampleObject(
                                        name = "존재하는 회원 이메일",
                                        summary = "EXIST_EMAIL 오류",
                                        value = """
                                                {
                                                  "success": false,
                                                  "message": "존재하는 회원 이메일입니다.",
                                                  "grpcServerStatus": "EXIST_EMAIL",
                                                  "http_status_code": 400
                                                }"""
                                ),
                                @ExampleObject(
                                        name = "존재하는 유저 해시값",
                                        summary = "EXIST_USERHASH 오류",
                                        value = """
                                                {
                                                  "success": false,
                                                  "message": "존재하는 유저 해시값입니다.",
                                                  "grpcServerStatus": "EXIST_USERHASH",
                                                  "http_status_code": 400
                                                }"""
                                ),
                                @ExampleObject(
                                        name = "존재하는 유저 UID",
                                        summary = "EXIST_UID 오류",
                                        value = """
                                                {
                                                  "success": false,
                                                  "message": "존재하는 유저 UID입니다.",
                                                  "grpcServerStatus": "EXIST_UID",
                                                  "http_status_code": 400
                                                }"""
                                ),
                                @ExampleObject(
                                        name = "유효하지 않은 회원 정보",
                                        summary = "INVALID_PARAMETER 오류",
                                        value = """
                                                {
                                                  "success": false,
                                                  "message": "유효하지 않은 회원 정보가 존재합니다.",
                                                  "grpcServerStatus": "INVALID_PARAMETER",
                                                  "http_status_code": 400
                                                }"""
                                )
                        }
                )
        )
})
public @interface UserRegisterBadRequestApiResponses {
}
