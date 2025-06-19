package org.zerock.voteservice.adapter.in.web.controller.query.docs.ballotQueryApiResponses;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "유저의 투표지 조회",
        description =         """
                   특정 사용자의 고유 해시값을 기반으로, 해당 사용자가 참여한 투표 기록 목록을 조회
                   
                    - 유저의 투표지 조회 요청 interface: Schemas [ QueryBallotRequestDto ] 참조
                   
                    - 유저의 투표지 조회 응답 interface: Schemas [ QueryBallotResponseDto ] 참조
                    """,
        tags = {"투표지 조회"},
        parameters = {
                @Parameter(
                        name = "userHash",
                        in = ParameterIn.PATH,
                        description = "조회할 사용자의 고유 해시값",
                        example = "e78ee58ca6cd9b4e9d167332845fb458f444f62bc9ac35b0b3c3d6079dfbbed9",
                        required = true,
                        schema = @Schema(type = "string", format = "HEX")
                )
        }
)
public @interface QueryBallotApiOperation {
}
