package org.zerock.voteservice.dto;

import lombok.Data;

@Data
public class VoteProposalDto {
    private String topic;
    private int duration;
}
