package org.zerock.voteservice.controller.mapper.base;

public final class ApiEndpoints {
    private ApiEndpoints() {}

    public static final String API_ROOT = "/api";
    public static final String API_VERSION = "/v1";

    public static final String API_USER_ENDPOINT = API_ROOT + API_VERSION + "/user";
    public static final String API_VOTE_ENDPOINT = API_ROOT + API_VERSION + "/vote";
    public static final String API_EVENT_ENDPOINT = API_ROOT + API_VERSION + "/event";
    public static final String API_QUERY_ENDPOINT = API_ROOT + API_VERSION + "/query";
}