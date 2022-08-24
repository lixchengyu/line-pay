package com.lance.dto.confirm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lance.dto.common.ResponseDto;
import com.lance.dto.confirm.bean.Info;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@ToString
public class ConfirmResponseDto extends ResponseDto {

    private Info info;
}
