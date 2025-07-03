package org.zerock.voteservice.adapter.in.web.processor;

import domain.event.ballot.query.protocol.Ballot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.zerock.voteservice.adapter.in.common.ProcessorData;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class BallotQueryProcessorData implements ProcessorData {
    private List<Ballot> ballotList;
}
