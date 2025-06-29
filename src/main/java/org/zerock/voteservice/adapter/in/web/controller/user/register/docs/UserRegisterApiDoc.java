package org.zerock.voteservice.adapter.in.web.controller.user.register.docs;

import org.zerock.voteservice.adapter.in.web.controller.user.register.docs.userRegisterApiDescriptions.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@UserRegisterApiOperation
@UserRegisterOkApiResponses
@UserRegisterBadRequestApiResponses
@UserRegisterInternalServerErrorApiResponses
public @interface UserRegisterApiDoc {
}
