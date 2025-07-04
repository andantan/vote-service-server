package org.zerock.voteservice.adapter.in.web.domain.dto.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.zerock.voteservice.adapter.in.web.domain.dto.CommonResponseDto;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
//@NoArgsConstructor
@AllArgsConstructor
public class ProposalCachingFailureResponseDto extends CommonResponseDto {
}
