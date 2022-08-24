package com.lance.dto.confirm.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@ToString
public class PayInfo {

    private String method;
    private Integer amount;
    private String creditCardNickname;
    private String creditCardBrand;
    private String maskedCreditCardNumber;

}
