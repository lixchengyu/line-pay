package com.lance.dto.confirm.bean;

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

    private String orderId;
    private String transactionId;
    private String authorizationExpireDate;
    private String regKey;
    private List<PayInfo> payInfo;
    private List<Package> packages;
}
