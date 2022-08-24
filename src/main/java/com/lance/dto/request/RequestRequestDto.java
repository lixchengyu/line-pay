package com.lance.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.lance.dto.request.bean.Options;
import com.lance.dto.request.bean.Package;
import com.lance.dto.request.bean.RedirectUrl;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
@Getter
@Setter
@ToString
public class RequestRequestDto {

    private Integer amount;
    private String currency;
    private String orderId;
    private List<Package> packages;
    private RedirectUrl redirectUrls;
    private Options options;
}
