package org.zerock.voteservice.adapter.in.web.processor.helper;

import domain.event.ballot.query.protocol.Ballot;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.zerock.voteservice.adapter.in.web.domain.schema.BallotSchema;
import org.zerock.voteservice.tool.time.DateConverter;

import java.time.LocalDateTime;

@Log4j2
@Component
public class BallotQueryProcessorHelper {
    public BallotSchema mapToBallotSchema(Ballot ballot) {
        LocalDateTime kstSubmittedAt = null;

        try {
            kstSubmittedAt = DateConverter.toKstLocalDateTime(ballot.getSubmittedAt());
        } catch (NullPointerException ignorable) {
            log.warn("submitted_at field is missing for voteHash: {}", ballot.getVoteHash());
        }

        return BallotSchema.builder()
                .voteHash(ballot.getVoteHash())
                .topic(ballot.getTopic())
                .submittedAt(kstSubmittedAt)
                .build();
    }
}
