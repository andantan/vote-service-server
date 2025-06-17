package org.zerock.voteservice.controller.query.docs.ballotQueryApiResponses;

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
        summary = "사용자 투표 기록 조회",
        description = "특정 사용자의 고유 해시값을 기반으로, 해당 사용자가 참여한 투표 기록 목록을 조회",
        tags = {"사용자 투표 기록 조회"},
        parameters = {
                @Parameter(
                        name = "userHash",
                        in = ParameterIn.PATH,
                        description = "조회할 사용자의 고유 해시값",
                        example = "1303522e0b59179b948c1713105ac8f9d13c62080a743118617fecef799e92e5",
                        required = true,
                        schema = @Schema(type = "string", format = "hex")
                )
        }
)
public @interface QueryBallotApiOperation {
}
