package org.zerock.voteservice.adapter.in.web.controller.query.proposal.docs;

import org.zerock.voteservice.adapter.in.web.controller.query.proposal.docs.proposalFilteredListApiResponses.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@QueryProposalFilteredListApiOperation
@QueryProposalFilteredListOkApiResponses
@QueryProposalFilteredListBadRequestApiResponses
@QueryProposalFilteredLIstInternalServerErrorApiResponses
public @interface QueryProposalFilteredListApiDoc {
}
