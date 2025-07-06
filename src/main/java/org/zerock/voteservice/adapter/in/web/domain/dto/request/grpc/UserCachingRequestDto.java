package org.zerock.voteservice.adapter.in.web.domain.dto.request.grpc;

import lombok.*;
import com.google.protobuf.Timestamp;
import org.zerock.voteservice.adapter.in.common.extend.GrpcRequestDto;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCachingRequestDto implements GrpcRequestDto {
    private Integer uid;
    private String userHash;
    private String gender;
    private Timestamp birthDate;
}
