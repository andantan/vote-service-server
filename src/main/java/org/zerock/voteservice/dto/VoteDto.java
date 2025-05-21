package org.zerock.voteservice.dto;

import lombok.Data;

@Data
public class VoteDto {
    private String voteHash;
    private String voteOption;
    private String voteId;
}