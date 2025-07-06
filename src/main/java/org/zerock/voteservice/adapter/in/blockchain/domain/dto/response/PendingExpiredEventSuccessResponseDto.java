package org.zerock.voteservice.adapter.in.blockchain.domain.dto.response;

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
    private String voteId;
    private int voteCount;
    private Map<String, Integer> voteOptions;
}
