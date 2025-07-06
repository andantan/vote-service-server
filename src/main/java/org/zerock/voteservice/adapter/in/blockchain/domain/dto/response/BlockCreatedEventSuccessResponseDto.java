package org.zerock.voteservice.adapter.in.blockchain.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.zerock.voteservice.adapter.in.common.extend.CommonResponseDto;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BlockCreatedEventSuccessResponseDto extends CommonResponseDto {
    @JsonProperty("topic")
    private String topic;

    @JsonProperty("tx_count")
    private int txCount;

    @JsonProperty("height")
    private int height;
}
