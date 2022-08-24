package com.lance.dto.payment.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@ToString
public class Info {

    private String transactionId;
    private String transactionDate;
    private String transactionType;
    private String payStatus;
    private String productName;
    private String merchantName;
    private String currency;
    private String authorizationExpireDate;
    private List<PayInfo> payInfo;
    private MerchantReference merchantReference;
    private List<RefundList> refundList;
    private Integer originalTransactionId;
}
