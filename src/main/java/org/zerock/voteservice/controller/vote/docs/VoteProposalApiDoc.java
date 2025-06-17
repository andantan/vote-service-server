package org.zerock.voteservice.controller.vote.docs;

import org.zerock.voteservice.controller.vote.docs.proposalApiDescriptions.*;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ProposalApiOperation
@ProposalOkApiResponses
@ProposalBadRequestApiResponses
@ProposalConflictApiResponses
@ProposalInternalServerErrorApiResponses
public @interface VoteProposalApiDoc { }