package com.tuwalike.wedding.utils;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.tuwalike.wedding.models.NetworkResponse;

import lombok.RequiredArgsConstructor;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
@RequiredArgsConstructor
public class NetworkUtil {

    private final OkHttpClient okHttpClient;

    public NetworkResponse get(String url, Map<String, String> headers) {

        Request request = new Request.Builder()
                .url(url)
                .get()
                .headers(Headers.of(headers))
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            boolean isSuccess = HttpStatus.valueOf(response.code()).is2xxSuccessful();
            return NetworkResponse.builder().isSuccess(isSuccess).code(response.code())
                    .responseBody(response.body().string()).build();
        } catch (IOException e) {

            return NetworkResponse.builder().isSuccess(false).code(001)
                    .responseBody(String.format("Request timed out with error: %s", e.getMessage())).build();

        }

    }

    public NetworkResponse post(String url, String payload, MediaType mediaType, Map<String, String> headers) {

        RequestBody body;

        if (payload == null || mediaType == null) {
            body = RequestBody.create("", null);
        } else {
            body = RequestBody.create(payload, mediaType);
        }

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .headers(Headers.of(headers))
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            boolean isSuccess = HttpStatus.valueOf(response.code()).is2xxSuccessful();
            return NetworkResponse.builder().isSuccess(isSuccess).code(response.code())
                    .responseBody(response.body().string()).build();
        } catch (IOException e) {

            return NetworkResponse.builder().isSuccess(false).code(001)
                    .responseBody(String.format("Request timed out with error: %s", e.getMessage())).build();
        }
    }

    public NetworkResponse postForm(String url, RequestBody payload) {

        Request request = new Request.Builder()
                .url(url)
                .post(payload)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            boolean isSuccess = HttpStatus.valueOf(response.code()).is2xxSuccessful();
            return NetworkResponse.builder().isSuccess(isSuccess).code(response.code())
                    .responseBody(response.body().string()).build();
        } catch (IOException e) {

            return NetworkResponse.builder().isSuccess(false).code(001)
                    .responseBody(String.format("Request timed out with error: %s", e.getMessage())).build();
        }
    }

}
