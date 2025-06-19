package org.zerock.voteservice.adapter.in.web.dto.query.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
        description = "블록 데이터 인터페이스"
)
public class BlockHeightSchema {
    @JsonProperty("height")
    @Schema(
            description = "투표(Pending)의 투표지(Transaction)가 저장된 블록의 높이",
            example = "1000"
    )
    private Integer height;

    @JsonProperty("length")
    @Schema(
            description = "해당 블록에 저장된 투표지(Transaction)의 개수",
            example = "9500"
    )
    private Integer length;
}
