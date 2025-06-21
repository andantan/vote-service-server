package org.zerock.voteservice.adapter.in.web.controller.query.ballot.docs;

import org.zerock.voteservice.adapter.in.web.controller.query.ballot.docs.ballotQueryApiResponses.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@QueryBallotApiOperation
@QueryBallotOkApiResponses
@QueryBallotBadRequestApiResponses
@QueryBallotNotFoundApiResponses
@QueryBallotInternalServerErrorApiResponses
public @interface QueryBallotApiDoc {
}
