package com.lance.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lance.dto.common.ResponseDto;
import com.lance.dto.request.bean.PaymentUrl;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@ToString
public class RequestResponseDto extends ResponseDto {

    private PaymentUrl paymentUrl;
    private String transactionId;
    private String paymentAccessToken;
}
