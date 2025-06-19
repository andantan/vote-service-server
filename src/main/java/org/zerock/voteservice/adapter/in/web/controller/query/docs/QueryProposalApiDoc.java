package org.zerock.voteservice.adapter.in.web.controller.query.docs;

import org.zerock.voteservice.adapter.in.web.controller.query.docs.proposalDetailQueryApiResponses.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@QueryProposalApiOperation
@QueryProposalOkApiResponses
@QueryProposalNotFoundApiResponses
@QueryProposalInternalServerErrorApiResponses
public @interface QueryProposalApiDoc {
}
