package org.zerock.voteservice.adapter.in.blockchain.dto.block;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BlockCreatedEventDto {
    @JsonProperty("topic")
    private String topic;

    @JsonProperty("transaction_count")
    private int transactionCount;

    @JsonProperty("height")
    private int height;
}
