package org.zerock.voteservice.adapter.in.web.domain.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultSchema {

    @JsonProperty("count")
    private Integer count;

    @JsonProperty("options")
    private Map<String, Integer> options;
}