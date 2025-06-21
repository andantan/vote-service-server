package org.zerock.voteservice.adapter.in.web.controller.vote.submit.docs;

import org.zerock.voteservice.adapter.in.web.controller.vote.submit.docs.voteSubmitApiDescriptions.*;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@VoteSubmitApiOperation
@VoteSubmitOkApiResponses
@VoteSubmitBadRequestApiResponses
@VoteSubmitNotFoundApiResponses
@VoteSubmitNotAcceptableApiResponses
@VoteSubmitConflictApiResponses
@VoteSubmitInternalServerErrorApiResponses
public @interface VoteSubmitApiDoc { }
