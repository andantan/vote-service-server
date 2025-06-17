package org.zerock.voteservice.controller.query.docs;

import org.zerock.voteservice.controller.query.docs.ballotQueryApiResponses.*;

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
