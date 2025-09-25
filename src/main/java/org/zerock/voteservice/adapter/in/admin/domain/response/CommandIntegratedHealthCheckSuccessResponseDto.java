package org.zerock.voteservice.adapter.in.admin.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.zerock.voteservice.adapter.in.common.ResponseDto;

import java.util.List;

@Data
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CommandIntegratedHealthCheckSuccessResponseDto implements ResponseDto {
    @JsonProperty("success_L3")
    private Boolean successL3;

    @JsonProperty("message_L3")
    private String messageL3;

    @JsonProperty("status_L3")
    private String statusL3;

    @JsonProperty("http_status_code_L3")
    private Integer httpStatusCodeL3;

    @JsonProperty("ping_L3")
    private String pingL3;

    @JsonProperty("pong_L3")
    private String pongL3;

    @JsonProperty("ip_L3")
    private String ipL3;

    @JsonProperty("ports_L3")
    private List<Integer> portsL3;

    @JsonProperty("success_L4")
    private Boolean successL4;

    @JsonProperty("message_L4")
    private String messageL4;

    @JsonProperty("status_L4")
    private String statusL4;

    @JsonProperty("http_status_code_L4")
    private Integer httpStatusCodeL4;

    @JsonProperty("ping_L4")
    private String pingL4;

    @JsonProperty("pong_L4")
    private String pongL4;

    @JsonProperty("ip_L4")
    private String ipL4;

    @JsonProperty("ports_L4")
    private List<Integer> portsL4;
}
