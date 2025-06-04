package org.zerock.voteservice.controller.vote;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/vote")
@CrossOrigin(origins = "http://localhost:3000")
public abstract class VoteRequestMapper {
}
