package org.zerock.voteservice.adapter.out.grpc.common;

public interface GrpcGracefullyShutDownHook {
    void shutdown();
}
