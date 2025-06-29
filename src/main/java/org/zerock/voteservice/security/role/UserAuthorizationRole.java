package org.zerock.voteservice.security.role;

import lombok.Getter;

@Getter
public enum UserAuthorizationRole {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    VISITOR("ROLE_VISITOR");

    private final String value;

    UserAuthorizationRole(String value) {
        this.value = value;
    }
}
