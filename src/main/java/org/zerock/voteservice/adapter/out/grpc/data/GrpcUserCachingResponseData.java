package org.zerock.voteservice.adapter.out.grpc.data;

import domain.event.user.create.protocol.UserCacheEventResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zerock.voteservice.adapter.out.grpc.common.GrpcResponseData;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrpcUserCachingResponseData implements GrpcResponseData {
    Boolean success;
    String status;
    Long uid;
    String userHash;

    public GrpcUserCachingResponseData(UserCacheEventResponse response) {
        this.success = response.getCached();
        this.status = response.getStatus();
        this.uid = response.getUid();
        this.userHash = response.getUserHash();
    }
}
