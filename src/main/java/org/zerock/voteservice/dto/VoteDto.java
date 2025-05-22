package org.zerock.voteservice.dto;

import lombok.Data;

@Data
public class VoteDto {
    private String hash;
    private String option;
    private String topic;
}