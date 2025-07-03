package org.zerock.voteservice.adapter.in.common;

import lombok.Getter;

@Getter
public enum ServiceLayer {
    WAS("L2", "Layer-2", "Web Application Server"),

    // Downstream servers
    MONGODB_CACHE("L3", "Layer-3", "MongoDB-Cache Server"),
    BLOCKCHAIN_NODE("L4", "Layer-4", "Blockchain-Node Server");

    private final String layerCode;
    private final String layerName;
    private final String fullName;

    ServiceLayer(String layerCode, String LayerName, String fullName) {
        this.layerCode = layerCode;
        this.layerName = LayerName;
        this.fullName = fullName;
    }

    public static ServiceLayer fromLayerCode(String code) {
        for (ServiceLayer layer : values()) {
            if (layer.layerCode.equalsIgnoreCase(code)) {
                return layer;
            }
        }

        throw new IllegalArgumentException("Unknown service layer code: " + code);
    }
}
