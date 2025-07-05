package org.zerock.voteservice.adapter.in.web.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.zerock.voteservice.adapter.in.common.CommonResponseDto;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@AllArgsConstructor
public class CommonFailureResponseDto extends CommonResponseDto {
}
