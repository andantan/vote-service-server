package org.zerock.voteservice.controller.event;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.voteservice.dto.event.ExpiredPendingEventDto;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestController
public class ExpiredPendingEventController extends EventRequestMapper {
    @PostMapping("/expired-pending")
    public Map<String, Object> eventNewBlock(@RequestBody ExpiredPendingEventDto dto) {
        log.info("Expired pending event received: votingId='{}', submitsLength={}, submitsOption={}",
                dto.getVotingId(),
                dto.getSubmitsLength(),
                dto.getSubmitsOption()
        );

        if (dto.getSubmitsOption() != null && !dto.getSubmitsOption().isEmpty()) {
            log.info("  Submits Option Details:");
            dto.getSubmitsOption().forEach((key, value) ->
                    log.info("    Option '{}': {} submissions", key, value));
        }

        Map<String, Object> result = new HashMap<>();

        result.put("caching", true);
        result.put("voting_id", dto.getVotingId());

        return result;
    }
}
