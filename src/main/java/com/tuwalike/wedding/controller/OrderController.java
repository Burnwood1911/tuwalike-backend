package com.tuwalike.wedding.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tuwalike.wedding.models.GeneralResponse;
import com.tuwalike.wedding.models.requests.CreatePaymentRequest;
import com.tuwalike.wedding.models.requests.GetPaymentStatusRequest;
import com.tuwalike.wedding.service.OrderService;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("pay")
    public ResponseEntity<GeneralResponse> pay(@RequestBody CreatePaymentRequest request) {

        GeneralResponse result = orderService.pay(request);

        return new ResponseEntity<>(result,
                HttpStatus.valueOf(result.getStatusCode()));
    }

    @PostMapping("callback")
    public ResponseEntity<GeneralResponse> callback(@RequestBody Map<String, Object> requestData) {

        log.info("Callback Data: {}", requestData);

        GeneralResponse result = GeneralResponse.builder().statusCode(200).message("success").build();

        return new ResponseEntity<>(result,
                HttpStatus.valueOf(result.getStatusCode()));
    }

    @PostMapping("get-payment-status")
    public ResponseEntity<GeneralResponse> getPaymentStatus(@RequestBody GetPaymentStatusRequest request) {

        GeneralResponse result = orderService.getPaymentStatus(request);

        return new ResponseEntity<>(result,
                HttpStatus.valueOf(result.getStatusCode()));
    }
}
