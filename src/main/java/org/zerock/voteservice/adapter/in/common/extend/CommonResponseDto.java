package org.zerock.voteservice.adapter.in.common.extend;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.zerock.voteservice.adapter.in.common.ResponseDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class CommonResponseDto implements ResponseDto {
    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("message")
    private String message;

    @JsonProperty("status")
    private String status;

    @JsonProperty("http_status_code")
    private Integer httpStatusCode;
}
