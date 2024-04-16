package com.tuwalike.wedding.service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.tuwalike.wedding.entity.Order;
import com.tuwalike.wedding.models.GeneralResponse;
import com.tuwalike.wedding.models.NetworkResponse;
import com.tuwalike.wedding.models.requests.CreatePaymentRequest;
import com.tuwalike.wedding.models.requests.GetPaymentStatusRequest;
import com.tuwalike.wedding.repository.OrderRepository;
import com.tuwalike.wedding.utils.MD5Generator;
import com.tuwalike.wedding.utils.NetworkUtil;

import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final NetworkUtil networkUtil;

    private final OrderRepository orderRepository;

    public GeneralResponse pay(CreatePaymentRequest createPaymentRequest) {

        Order order = new Order();
        order.setEventId(createPaymentRequest.getEventId());

        JSONObject payload = new JSONObject()
                .put("api_source", "TUALIKE")
                .put("api_to", "TigoPesa")
                .put("amount", 1000)
                .put("product", "Wedding Card")
                .put("callback", "https://popo.alexrossi.xyz/api/v1/orders/callback")
                .put("hash", MD5Generator.generateHash("tualike"))
                .put("codex", "3342wfd")
                .put("user", "tualike")
                .put("mobileNo", "255718261948")
                .put("reference", UUID.randomUUID().toString());

        String URL = String.format("https://vodaapi.evmak.com/test/");

        Map<String, String> headers = Map.of(
                "Content-Type", "application/json",
                "Accept", "*/*");

        NetworkResponse networkResponse = networkUtil.post(URL, payload.toString(),
                MediaType.parse("application/json; charset=utf-8"), headers);

        if (networkResponse.isSuccess) {
            order.setStatus("PENDING");
        } else {
            order.setStatus("FAILED");
        }

        orderRepository.save(order);

        if (networkResponse.isSuccess) {
            return GeneralResponse.builder().statusCode(200).message("success")
                    .build();
        } else {
            return GeneralResponse.builder().statusCode(500).message("failed")
                    .build();
        }

    }

    public GeneralResponse getPaymentStatus(GetPaymentStatusRequest getPaymentStatusRequest) {

        Optional<Order> order = orderRepository.findById(getPaymentStatusRequest.getOrderId());

        if (order.isPresent()) {
            return GeneralResponse.builder().statusCode(200).message("success").data(order.get()).build();
        } else {
            return GeneralResponse.builder().statusCode(500).message("failed").build();
        }
    }
}
