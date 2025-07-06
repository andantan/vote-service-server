package org.zerock.voteservice.adapter.out.grpc.data;

import domain.event.user.create.protocol.UserValidateEventResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zerock.voteservice.adapter.out.grpc.common.GrpcResponseData;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrpcUserValidationResponseData implements GrpcResponseData {
    Boolean success;
    String status;

    public GrpcUserValidationResponseData(UserValidateEventResponse response) {
        this.success = response.getValidation();
        this.status = response.getStatus();
    }
}
