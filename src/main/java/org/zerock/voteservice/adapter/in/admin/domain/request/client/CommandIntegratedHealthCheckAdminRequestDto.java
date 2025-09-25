package org.zerock.voteservice.adapter.in.admin.domain.request.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.zerock.voteservice.adapter.in.common.extend.AdminRequestDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommandIntegratedHealthCheckAdminRequestDto implements AdminRequestDto {
    @JsonProperty("ping")
    private String ping;


    @Override
    public String command() {
        return "HealthCheck(Integrated)";
    }
}
