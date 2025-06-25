package org.zerock.voteservice.adapter.in.web.dto.user.role;

import lombok.Getter;

@Getter
public enum UserRole {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    VISITOR("ROLE_VISITOR");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }
}
