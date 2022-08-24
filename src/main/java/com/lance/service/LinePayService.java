package com.lance.service;

import com.google.gson.Gson;
import com.lance.dto.common.ResponseDto;
import com.lance.dto.confirm.ConfirmRequestDto;
import com.lance.dto.confirm.ConfirmResponseDto;
import com.lance.dto.confirm.bean.ConfirmRequest;
import com.lance.dto.confirm.bean.ConfirmResponse;
import com.lance.dto.payment.PaymentResponseDto;
import com.lance.dto.payment.bean.PaymentResponse;
import com.lance.dto.refund.RefundRequestDto;
import com.lance.dto.refund.RefundResponseDto;
import com.lance.dto.refund.bean.RefundRequest;
import com.lance.dto.refund.bean.RefundResponse;
import com.lance.dto.request.RequestRequestDto;
import com.lance.dto.request.RequestResponseDto;
import com.lance.dto.request.bean.RequestResponse;
import com.lance.util.HmacSignUtil;
import com.lance.util.HttpUtil;
import com.lance.util.MsgConf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class LinePayService {

    private static final Logger logger = LogManager.getLogger(LinePayService.class);

    @Value("${line-pay.channelId}")
    private String channelId;

    @Value("${line-pay.channelSecretKey}")
    private String channelSecretKey;

    @Value("${line-pay.url}")
    private String url;

    public ResponseDto postRequest(RequestRequestDto requestDto) throws Exception {
        RequestResponseDto responseDto = new RequestResponseDto();

        // Parameters
        String uri = "/v3/payments/request";
        String nonce = UUID.randomUUID().toString();
        String requestStr = new Gson().toJson(requestDto);
        Map<String, String> headers = getHeaders(channelSecretKey, channelId, uri, requestStr, nonce);

        // Post request
        HttpUtil.HttpResult httpResult = HttpUtil.post(url + uri, headers, requestStr);
        if (httpResult != null && httpResult.getBody() != null) {
            String responseStr = httpResult.getBody();
            RequestResponse response = new Gson().fromJson(responseStr, RequestResponse.class);
            responseDto.setCode(response.getReturnCode());
            responseDto.setMessage(response.getReturnMessage());
            if (response.getReturnCode().equals("0000")) {
                responseDto.setPaymentUrl(response.getInfo().getPaymentUrl());
                responseDto.setTransactionId(response.getInfo().getTransactionId());
                responseDto.setPaymentAccessToken(response.getInfo().getPaymentAccessToken());
            }
            logger.debug("HttpEntity: {}", responseStr);
        } else {
            responseDto.setCode(MsgConf.ResponseCode.ER0101.getCode());
            responseDto.setMessage(MsgConf.ResponseCode.ER0101.getMessage());
        }
        return responseDto;
    }

    public ResponseDto postConfirm(ConfirmRequestDto confirmRequestDto) throws Exception {
        ConfirmResponseDto responseDto = new ConfirmResponseDto();

        // Parameters
        String uri = "/v3/payments/" + confirmRequestDto.getTransactionId() + "/confirm";
        String nonce = UUID.randomUUID().toString();
        String requestStr = new Gson().toJson(new ConfirmRequest(confirmRequestDto.getAmount(), confirmRequestDto.getCurrency()));
        Map<String, String> headers = getHeaders(channelSecretKey, channelId, uri, requestStr, nonce);

        // Post request
        HttpUtil.HttpResult httpResult = HttpUtil.post(url + uri, headers, requestStr);
        if (httpResult != null && httpResult.getBody() != null) {
            String responseStr = httpResult.getBody();
            ConfirmResponse response = new Gson().fromJson(responseStr, ConfirmResponse.class);
            responseDto.setCode(response.getReturnCode());
            responseDto.setMessage(response.getReturnMessage());
            if (response.getReturnCode().equals("0000")) {
                responseDto.setInfo(response.getInfo());
            }
            logger.debug("HttpEntity: {}", responseStr);
        } else {
            responseDto.setCode(MsgConf.ResponseCode.ER0102.getCode());
            responseDto.setMessage(MsgConf.ResponseCode.ER0102.getMessage());
        }
        return responseDto;
    }

    public ResponseDto postRefund(RefundRequestDto refundRequestDto) throws Exception {
        RefundResponseDto responseDto = new RefundResponseDto();

        // Parameters
        String uri = "/v3/payments/" + refundRequestDto.getTransactionId() + "/refund";
        String nonce = UUID.randomUUID().toString();
        String requestStr = new Gson().toJson(new RefundRequest(refundRequestDto.getAmount(), refundRequestDto.getOptions()));
        Map<String, String> headers = getHeaders(channelSecretKey, channelId, uri, requestStr, nonce);

        // Post request
        HttpUtil.HttpResult httpResult = HttpUtil.post(url + uri, headers, requestStr);
        if (httpResult != null && httpResult.getBody() != null) {
            String responseStr = httpResult.getBody();
            RefundResponse response = new Gson().fromJson(responseStr, RefundResponse.class);
            responseDto.setCode(response.getReturnCode());
            responseDto.setMessage(response.getReturnMessage());
            if (response.getReturnCode().equals("0000")) {
                responseDto.setInfo(response.getInfo());
            }
            logger.debug("HttpEntity: {}", responseStr);
        } else {
            responseDto.setCode(MsgConf.ResponseCode.ER0103.getCode());
            responseDto.setMessage(MsgConf.ResponseCode.ER0103.getMessage());
        }
        return responseDto;
    }

    public ResponseDto getPayments(List<String> transactionId, List<String> orderId, List<String> fields) throws Exception {
        PaymentResponseDto responseDto = new PaymentResponseDto();

        // Parameters
        Map<String, List<String>> paramMap = new HashMap<>();
        if (transactionId != null)
            paramMap.put("transactionId", transactionId);
        if (orderId != null)
            paramMap.put("orderId", orderId);
        if (fields != null)
            paramMap.put("fields", fields);
        String queryString = getQueryString(paramMap);
        logger.info("Query string: {}", queryString);
        String uri = "/v3/payments";
        String nonce = UUID.randomUUID().toString();
        Map<String, String> headers = getHeaders(channelSecretKey, channelId, uri, queryString, nonce);

        // Get request
        HttpUtil.HttpResult httpResult = HttpUtil.get(url + uri, queryString, headers);
        if (httpResult != null && httpResult.getBody() != null) {
            String responseStr = httpResult.getBody();
            PaymentResponse response = new Gson().fromJson(responseStr, PaymentResponse.class);
            responseDto.setCode(response.getReturnCode());
            responseDto.setMessage(response.getReturnMessage());
            if (response.getReturnCode().equals("0000")) {
                responseDto.setInfo(response.getInfo());
            }
            logger.debug("HttpEntity: {}", responseStr);
        } else {
            responseDto.setCode(MsgConf.ResponseCode.ER0104.getCode());
            responseDto.setMessage(MsgConf.ResponseCode.ER0104.getMessage());
        }
        return responseDto;
    }

    private static Map<String, String> getHeaders(String channelSecretKey, String channelId, String uri, String requestStr, String nonce) throws Exception {
        // Set authorization text
        String authText = HmacSignUtil.getAuthTex(channelSecretKey, uri, requestStr, nonce);
        String hmacText = HmacSignUtil.encrypt(channelSecretKey, authText);

        // Set headers
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-LINE-ChannelId", channelId);
        headers.put("X-LINE-Authorization-Nonce", nonce);
        headers.put("X-LINE-Authorization", hmacText);
        return headers;
    }

    private static String getQueryString(Map<String, List<String>> params) {
        if (params != null && !params.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            params.forEach((k, v) -> {
                if (v != null) v.forEach(s -> sb.append("&").append(k).append("=").append(s));
            });
            return sb.substring(1);
        } else {
            return null;
        }
    }

}
