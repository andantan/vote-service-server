package org.zerock.voteservice.adapter.in.web.controller.docs.register.userRegisterApiDescriptions;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.zerock.voteservice.adapter.in.web.dto.UserRegisterRequestDto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Tag(name = "회원 가입", description = "신규 회원 등록 관련 API")
@SecurityRequirement(name = "bearerAuth")
@Operation(
        summary = "사용자 회원가입",
        description = """
                    신규 회원 등록 후 초기 사용자 정보 반환
                    
                    - 신규 회원 등록 요청 interface: Schemas [ UserRegisterRequestDto ] 참조
                    
                    - 신규 회원 등록 응답 interface: Schemas [ UserRegisterResponseDto ] 참조
                    """,
        requestBody = @RequestBody(
                description = "신규 회원 등록 요청 Body",
                required = true,
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = UserRegisterRequestDto.class),
                        examples = @ExampleObject(
                                name = "투표 제안 요청 예시",
                                summary = "새로운 투표 생성",
                                value = """
                                        {
                                          "username": "user123",
                                          "password": "password!@#123",
                                          "real_name": "홍길동",
                                          "resident_registration_number_part": "001209-3",
                                          "email": "user@example.com",
                                          "phone_number": "01012345678"
                                        }"""
                        )
                )
        )
)
public @interface UserRegisterApiOperation {
}
