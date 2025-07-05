package org.zerock.voteservice.adapter.in.web.domain.dto.request;

import lombok.*;
import com.google.protobuf.Timestamp;
import org.zerock.voteservice.adapter.in.common.RequestDto;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCachingRequestDto implements RequestDto {
    private Integer uid;
    private String userHash;
    private String gender;
    private Timestamp birthDate;
}
