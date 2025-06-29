package org.zerock.voteservice.adapter.in.web.controller.vote.proposal.docs;

import org.zerock.voteservice.adapter.in.web.controller.vote.proposal.docs.voteproposalApiDescriptions.*;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@VoteProposalApiOperation
@VoteProposalOkApiResponses
@VoteProposalBadRequestApiResponses
@VoteProposalConflictApiResponses
@VoteProposalInternalServerErrorApiResponses
public @interface VoteProposalApiDoc { }