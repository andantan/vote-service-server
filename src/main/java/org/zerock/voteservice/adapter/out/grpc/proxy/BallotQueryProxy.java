package org.zerock.voteservice.adapter.out.grpc.proxy;

import domain.event.ballot.query.protocol.GetUserBallotsResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.zerock.voteservice.adapter.in.web.domain.dto.QueryBallotRequestDto;
import org.zerock.voteservice.adapter.out.grpc.stub.BallotQueryEventServiceGrpcStub;

@Log4j2
@Service
public class BallotQueryProxy {
    private final BallotQueryEventServiceGrpcStub ballotQueryEventServiceGrpcStub;

    public BallotQueryProxy(
            BallotQueryEventServiceGrpcStub ballotQueryEventServiceGrpcStub
    ) {
        this.ballotQueryEventServiceGrpcStub = ballotQueryEventServiceGrpcStub;
    }

    public GetUserBallotsResponse getUserBallots(QueryBallotRequestDto dto) {
        return this.ballotQueryEventServiceGrpcStub.getUserBallots(dto.getUserHash());
    }
}
