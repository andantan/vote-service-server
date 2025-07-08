package org.zerock.voteservice.adapter.in.admin.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.zerock.voteservice.adapter.in.common.extend.CommonResponseDto;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CommandL4HealthCheckSuccessResponseDto extends CommonResponseDto {
    @JsonProperty("ping")
    private String ping;

    @JsonProperty("pong")
    private String pong;

    @JsonProperty("ip")
    private String ip;

    @JsonProperty("ports")
    private List<Integer> ports;
}
