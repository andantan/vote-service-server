package org.zerock.voteservice;

import lombok.Data;

@Data
public class VoteDto {
    private String voteHash;
    private String voteOption;
    private String electionId;
}