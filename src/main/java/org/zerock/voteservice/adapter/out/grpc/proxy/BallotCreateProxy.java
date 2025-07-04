package org.zerock.voteservice.adapter.out.grpc.proxy;

import domain.event.ballot.create.protocol.BallotCacheEventResponse;
import domain.event.ballot.create.protocol.BallotValidateEventResponse;
import domain.vote.submit.protocol.SubmitBallotTransactionResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.zerock.voteservice.adapter.in.web.domain.dto.VoteSubmitBallotDto;
import org.zerock.voteservice.adapter.out.grpc.stub.BallotTransactionServiceGrpcStub;
import org.zerock.voteservice.adapter.out.grpc.stub.BallotCreateEventServiceGrpcStub;

@Log4j2
@Service
public class BallotCreateProxy {
    private final BallotTransactionServiceGrpcStub ballotTransactionServiceGrpcStub;
    private final BallotCreateEventServiceGrpcStub ballotCreateEventServiceGrpcStub;

    public BallotCreateProxy(
            BallotTransactionServiceGrpcStub ballotTransactionServiceGrpcStub,
            BallotCreateEventServiceGrpcStub ballotCreateEventServiceGrpcStub
    ) {
        this.ballotTransactionServiceGrpcStub = ballotTransactionServiceGrpcStub;
        this.ballotCreateEventServiceGrpcStub = ballotCreateEventServiceGrpcStub;
    }

    public BallotValidateEventResponse validateBallot(VoteSubmitBallotDto dto) {
        return this.ballotCreateEventServiceGrpcStub.validateBallot(
                dto.getUserHash(), dto.getTopic(), dto.getOption()
        );
    }

    public SubmitBallotTransactionResponse submitBallotTransaction(VoteSubmitBallotDto dto) {
        return this.ballotTransactionServiceGrpcStub.submitBallotTransaction(
                dto.getUserHash(), dto.getTopic(), dto.getOption()
        );
    }

    public BallotCacheEventResponse cacheBallot(VoteSubmitBallotDto dto, String voteHash) {
        return this.ballotCreateEventServiceGrpcStub.cacheBallot(
                dto.getUserHash(), voteHash, dto.getTopic()
        );
    }
}
