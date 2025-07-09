package org.zerock.voteservice.adapter.in.blockchain.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.zerock.voteservice.adapter.in.common.extend.CommonResponseDto;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PendingExpiredEventSuccessResponseDto extends CommonResponseDto {
    @JsonProperty("vote_id")
    private String voteId;

    @JsonProperty("vote_count")
    private int voteCount;

    @JsonProperty("vote_options")
    private Map<String, Integer> voteOptions;
}
