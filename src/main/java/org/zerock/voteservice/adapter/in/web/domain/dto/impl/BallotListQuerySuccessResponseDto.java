package org.zerock.voteservice.adapter.in.web.domain.dto.impl;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;
import lombok.experimental.SuperBuilder;

import org.zerock.voteservice.adapter.in.web.domain.dto.CommonResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.schema.BallotSchema;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BallotListQuerySuccessResponseDto extends CommonResponseDto {
    @JsonProperty("user_hash")
    private String userHash;

    @JsonProperty("ballot_list")
    private List<BallotSchema> ballotList;

    @JsonProperty("ballot_list_length")
    private Integer ballotListLength;
}
