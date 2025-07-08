package org.zerock.voteservice.adapter.out.grpc.data;

import domain.event.admin.L3.protocol.L3HealthCheckResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zerock.voteservice.adapter.out.grpc.common.GrpcResponseData;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrpcCommandL3HealthCheckResponseData implements GrpcResponseData {
    Boolean success;
    String status;
    String pong;
    String ip;
    List<Integer> ports;

    public GrpcCommandL3HealthCheckResponseData(L3HealthCheckResponse response) {
        this.success = response.getConnected();
        this.status = response.getStatus();
        this.pong = response.getPong();
        this.ip = response.getIp();
        this.ports = response.getPortsList();
    }
}
