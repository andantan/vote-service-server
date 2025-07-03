package org.zerock.voteservice.adapter.in.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.zerock.voteservice.adapter.in.common.extern.GrpcStatus;

@Getter
@Builder
@AllArgsConstructor
public class ProcessorResult<T extends ProcessorData> {
    private final GrpcStatus grpcStatus;    // Server status
    private final T data;   // Data

    public static <T extends ProcessorData> ProcessorResult<T> success(GrpcStatus grpcStatus, T data) {
        return ProcessorResult.<T>builder()
                .grpcStatus(grpcStatus)
                .data(data)
                .build();
    }

    public static <T extends ProcessorData> ProcessorResult<T> failure(GrpcStatus grpcStatus) {
        return ProcessorResult.<T>builder()
                .grpcStatus(grpcStatus)
                .data(null)
                .build();
    }

    public boolean isServerError() {
        return !grpcStatus.isSuccess() || this.data == null;
    }
}
