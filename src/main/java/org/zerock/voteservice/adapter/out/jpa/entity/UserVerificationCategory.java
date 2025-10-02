package org.zerock.voteservice.adapter.out.jpa.entity;

import lombok.Getter;

@Deprecated(since = "2025-10-02")
@Getter
public enum UserVerificationCategory {
    CATEGORY_REGISTER("REGISTER"),
    CATEGORY_RESET_PASSWORD("RESET_PASSWORD");

    private final String category;

    UserVerificationCategory(String category) {
        this.category = category;
    }
}
