package com.tuwalike.wedding.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.tuwalike.wedding.config.GsonConfig;
import com.tuwalike.wedding.models.message.Messages;
import com.tuwalike.wedding.utils.NetworkUtil;

import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;

@Service
@RequiredArgsConstructor
public class SmsService {

    private final NetworkUtil networkUtil;

    private final GsonConfig gsonConfig;

    public void send(Messages messages) {

        String payload = gsonConfig.getGson().toJson(messages, Messages.class);

        Map<String, String> headers = Map.of("Content-Type", "application/json", "Accept", "application/json",
                "Authorization", "Basic RGFsb246VHVhbGlrZTEyMw==");

        String URL = "https://messaging-service.co.tz/api/sms/v1/text/multi";

        networkUtil.post(URL, payload, MediaType.parse("application/json"),
                headers);

    }
}
