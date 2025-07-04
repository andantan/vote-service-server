package org.zerock.voteservice.adapter.in.web.domain.data.impl;

import domain.event.ballot.query.protocol.GetUserBallotsResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

import domain.event.ballot.query.protocol.Ballot;

import org.zerock.voteservice.adapter.in.web.domain.data.GrpcResponseData;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrpcBallotListQueryResponseData implements GrpcResponseData {
    Boolean success;
    String status;
    List<Ballot> ballotList;

    public GrpcBallotListQueryResponseData(GetUserBallotsResponse response) {
        this.success = response.getQueried();
        this.status = response.getStatus();
        this.ballotList = response.getBallotsList();
    }

}
