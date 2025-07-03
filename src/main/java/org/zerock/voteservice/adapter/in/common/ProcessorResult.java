package org.zerock.voteservice.adapter.in.common;

public interface ProcessorResult<T extends ProcessorData> {
    Boolean getSuccess();
    String getMessage();
    DownstreamServiceResponseStatus getStatus();
    T getData();
}
