package org.zerock.voteservice.dto;

import lombok.Data;

@Data
public class VoteSubmitDto {
    private String hash;
    private String option;
    private String topic;
}