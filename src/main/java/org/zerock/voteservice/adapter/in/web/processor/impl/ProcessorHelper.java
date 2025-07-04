package org.zerock.voteservice.adapter.in.web.processor.impl;

import domain.event.ballot.query.protocol.Ballot;
import domain.event.proposal.query.protocol.BlockHeight;
import domain.event.proposal.query.protocol.Proposal;
import domain.event.proposal.query.protocol.Result;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.zerock.voteservice.adapter.in.web.domain.dto.schema.*;
import org.zerock.voteservice.tool.time.DateConverter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

@Log4j2
@Component
public class ProcessorHelper {

    public List<? extends ProposalResponseSchema> mapToFilteredProposalList(
            boolean summarized,
            List<Proposal> proposalList
    ) {
        Function<Proposal, ? extends ProposalResponseSchema> mapper = summarized
                ? this::mapToProposalSummarizedSchema
                : this::mapToProposalDetailSchema;

        return proposalList.stream()
                .map(mapper)
                .toList();
    }

    public ProposalSummarizedSchema mapToProposalSummarizedSchema(Proposal proposal) {
        LocalDateTime kstExpiredAt = DateConverter.toKstLocalDateTime(proposal.getExpiredAt());

        return ProposalSummarizedSchema.builder()
                .topic(proposal.getTopic())
                .expired(proposal.getExpired())
                .expiredAt(kstExpiredAt)
                .build();
    }

    public ProposalDetailSchema mapToProposalDetailSchema(Proposal proposal) {
        List<BlockHeightSchema> blockHeightSchemas = proposal.getBlockHeightsList().stream()
                .map(this::mapToBlockHeightSchema)
                .toList();

        ResultSchema resultSchema = this.mapToResultSchema(proposal.getResult());

        LocalDateTime kstCreatedAt = DateConverter.toKstLocalDateTime(proposal.getCreatedAt());
        LocalDateTime kstExpiredAt = DateConverter.toKstLocalDateTime(proposal.getExpiredAt());

        List<String> options = proposal.getOptionsList();

        return ProposalDetailSchema.builder()
                .topic(proposal.getTopic())
                .duration(proposal.getDuration())
                .expired(proposal.getExpired())
                .blockHeights(blockHeightSchemas)
                .result(resultSchema)
                .createdAt(kstCreatedAt)
                .expiredAt(kstExpiredAt)
                .options(options)
                .build();
    }

    public BlockHeightSchema mapToBlockHeightSchema(BlockHeight blockHeight) {
        return BlockHeightSchema.builder()
                .height(blockHeight.getHeight())
                .length(blockHeight.getLength())
                .build();
    }

    public ResultSchema mapToResultSchema(Result result) {
        return ResultSchema.builder()
                .count(result.getCount())
                .options(result.getOptionsMap())
                .build();
    }

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
