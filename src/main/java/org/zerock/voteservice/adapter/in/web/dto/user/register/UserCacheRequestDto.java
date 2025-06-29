package org.zerock.voteservice.adapter.in.web.dto.user.register;

import lombok.*;
import com.google.protobuf.Timestamp;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCacheRequestDto {
    private Integer uid;
    private String userHash;
    private String gender;
    private Timestamp birthDate;
}
