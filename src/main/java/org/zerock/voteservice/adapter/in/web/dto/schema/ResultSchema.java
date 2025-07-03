package org.zerock.voteservice.adapter.in.web.dto.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
        description = "투표 결과 스키마"
)
public class ResultSchema {

    @JsonProperty("count")
    @Schema(
            description = "총 투표 참여자 수 (또는 총 유효 투표 수)",
            example = "11285",
            type = "integer",
            format = "int32",
            implementation = Integer.class
    )
    private Integer count;

    @JsonProperty("options")
    @Schema(
            description = "각 투표 선택지별 득표 수",
            example = """
                    {
                      "1": 2253,
                      "2": 2204,
                      "3": 2253,
                      "4": 2254,
                      "5": 2321
                    }""",
            type = "object",
            implementation = Map.class,
            additionalProperties = Schema.AdditionalPropertiesValue.TRUE
    )
    private Map<String, Integer> options;
}