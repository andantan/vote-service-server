package org.zerock.voteservice.adapter.in.blockchain.domain.dto.requests.blockchain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.zerock.voteservice.adapter.in.common.extend.UnauthenticatedRequestDto;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BlockCreatedEventBlockchainRequestDto implements UnauthenticatedRequestDto {
    @JsonProperty("topic")
    private String topic;

    @JsonProperty("transaction_count")
    private int transactionCount;

    @JsonProperty("height")
    private int height;

    @Override
    public String identifier() {
        return String.format("CreatedBlockEvent:%d", height);
    }
}
