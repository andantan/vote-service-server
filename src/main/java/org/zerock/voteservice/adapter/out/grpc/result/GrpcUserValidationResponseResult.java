package org.zerock.voteservice.adapter.out.grpc.result;

import org.zerock.voteservice.adapter.out.grpc.common.AbstractGrpcResponseResult;
import org.zerock.voteservice.adapter.out.grpc.data.GrpcUserValidationResponseData;
import org.zerock.voteservice.adapter.out.grpc.status.GrpcUserValidationResponseStatus;

public class GrpcUserValidationResponseResult
    extends AbstractGrpcResponseResult<GrpcUserValidationResponseStatus, GrpcUserValidationResponseData> {
}
