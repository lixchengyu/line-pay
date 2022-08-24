package com.lance.controller;

import com.lance.dto.common.ResponseDto;
import com.lance.dto.confirm.ConfirmRequestDto;
import com.lance.dto.refund.RefundRequestDto;
import com.lance.dto.request.RequestRequestDto;
import com.lance.service.LinePayService;
import com.lance.util.MsgConf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "line-pay")
public class LinePayController {

    private static final Logger logger = LogManager.getLogger(LinePayController.class);

    private final LinePayService linePayService;

    @Autowired
    public LinePayController(LinePayService linePayService) {
        this.linePayService = linePayService;
    }


    @PostMapping(path = "/request", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> postRequest(@RequestBody RequestRequestDto requestDto) {
        logger.debug("LinePayController - postRequest {}", requestDto);
        try {
            ResponseDto response = linePayService.postRequest(requestDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error request", ex);
            return new ResponseEntity<>(new ResponseDto(MsgConf.ResponseCode.ER0001.getCode(),
                    MsgConf.ResponseCode.ER0001.getMessage()),
                    HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @PostMapping(path = "/confirm", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> postConfirm(@RequestBody ConfirmRequestDto requestDto) {
        logger.debug("LinePayController - postConfirm {}", requestDto);
        try {
            ResponseDto response = linePayService.postConfirm(requestDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error request", ex);
            return new ResponseEntity<>(new ResponseDto(MsgConf.ResponseCode.ER0001.getCode(),
                    MsgConf.ResponseCode.ER0001.getMessage()),
                    HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @PostMapping(path = "/refund", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> postRefund(@RequestBody RefundRequestDto requestDto) {
        logger.debug("LinePayController - postRefund {}", requestDto);
        try {
            ResponseDto response = linePayService.postRefund(requestDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error request", ex);
            return new ResponseEntity<>(new ResponseDto(MsgConf.ResponseCode.ER0001.getCode(),
                    MsgConf.ResponseCode.ER0001.getMessage()),
                    HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @GetMapping(path = "/payments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPayments(@RequestParam(required = false) List<String> transactionId,
                                         @RequestParam(required = false) List<String> orderId,
                                         @RequestParam(required = false) List<String> fields) {
        logger.debug("LinePayController - getPayments transactionId: {} orderId: {} fields: {}",
                transactionId, orderId, fields);
        try {
            ResponseDto response = linePayService.getPayments(transactionId, orderId, fields);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error request", ex);
            return new ResponseEntity<>(new ResponseDto(MsgConf.ResponseCode.ER0001.getCode(),
                    MsgConf.ResponseCode.ER0001.getMessage()),
                    HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}