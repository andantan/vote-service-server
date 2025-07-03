package org.zerock.voteservice.adapter.in.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DownstreamServiceResponseStatus {
    private final ServiceLayer serviceLayer;
    private final DownStreamServiceStatus downstreamStatus;

    public DownstreamServiceResponseStatus(ServiceLayer serviceLayer, DownStreamServiceStatus downstreamStatus) {
        this.serviceLayer = serviceLayer;
        this.downstreamStatus = downstreamStatus;
    }

    public static DownstreamServiceResponseStatus of(ServiceLayer layer, DownStreamServiceStatus downstreamStatus) {
        return new DownstreamServiceResponseStatus(layer, downstreamStatus);
    }

    public String getCode() {
        return this.downstreamStatus.getCode();
    }

    public HttpStatus getHttpStatus() {
        return this.downstreamStatus.getHttpStatusCode();
    }

    public String getMessageByLayerCode() {
        return this.downstreamStatus.getFormattedMessageByLayerCode(this.serviceLayer);
    }

    public String getMessageByLayerName() {
        return this.downstreamStatus.getFormattedMessageByLayerName(this.serviceLayer);
    }

    public String getMessageByFullName() {
        return this.downstreamStatus.getFormattedMessageByFullName(this.serviceLayer);
    }

    public boolean isSuccess() {
        return this.downstreamStatus == DownStreamServiceStatus.OK;
    }
}
