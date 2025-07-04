package org.zerock.voteservice.adapter.in.web.processor.impl;

import domain.event.proposal.query.protocol.BlockHeight;
import domain.event.proposal.query.protocol.Proposal;
import domain.event.proposal.query.protocol.Result;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.zerock.voteservice.adapter.in.web.domain.dto.impl.ProposalFilteredListQueryRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.schema.*;
import org.zerock.voteservice.adapter.in.web.processor.ProposalQueryProcessorResult;
import org.zerock.voteservice.tool.time.DateConverter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

@Log4j2
@Component
public class ProcessorHelper {

    public List<? extends ProposalResponseSchema> mappingFilteredProposalList(
            boolean summarized,
            List<Proposal> proposalList
    ) {
        Function<Proposal, ? extends ProposalResponseSchema> mapper = summarized
                ? this::mappingProposalSummarizedSchema
                : this::mappingProposalDetailSchema;

        return proposalList.stream()
                .map(mapper)
                .toList();
    }

    public ProposalSummarizedSchema mappingProposalSummarizedSchema(Proposal proposal) {
        LocalDateTime kstExpiredAt = DateConverter.toKstLocalDateTime(proposal.getExpiredAt());

        return ProposalSummarizedSchema.builder()
                .topic(proposal.getTopic())
                .expired(proposal.getExpired())
                .expiredAt(kstExpiredAt)
                .build();
    }

    public ProposalDetailSchema mappingProposalDetailSchema(Proposal proposal) {
        List<BlockHeightSchema> blockHeightSchemas = proposal.getBlockHeightsList().stream()
                .map(this::mappingBlockHeightSchema)
                .toList();

        ResultSchema resultSchema = this.mappingResultSchema(proposal.getResult());

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

    public BlockHeightSchema mappingBlockHeightSchema(BlockHeight blockHeight) {
        return BlockHeightSchema.builder()
                .height(blockHeight.getHeight())
                .length(blockHeight.getLength())
                .build();
    }

    public ResultSchema mappingResultSchema(Result result) {
        return ResultSchema.builder()
                .count(result.getCount())
                .options(result.getOptionsMap())
                .build();
    }
}
