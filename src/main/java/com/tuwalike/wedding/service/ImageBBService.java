package com.tuwalike.wedding.service;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.tuwalike.wedding.config.GsonConfig;
import com.tuwalike.wedding.models.NetworkResponse;
import com.tuwalike.wedding.models.imageupload.ImageUploadResponse;
import com.tuwalike.wedding.utils.NetworkUtil;

import lombok.RequiredArgsConstructor;
import okhttp3.FormBody;
import okhttp3.RequestBody;

@Service
@RequiredArgsConstructor
public class ImageBBService {

    private final NetworkUtil networkUtil;

    private final GsonConfig gsonConfig;

    public ImageUploadResponse upload(String image) {

        RequestBody formBody = new FormBody.Builder()
                .add("image", image)
                .build();

        String URL = "https://api.imgbb.com/1/upload?key=0a5003d6ef01f7d2790b9298c80b62fd";

        NetworkResponse response = networkUtil.postForm(URL, formBody);

        if (!response.isSuccess) {

            throw new RuntimeException("Failed to uplload image");
        }

        return gsonConfig.getGson().fromJson(response.getResponseBody(), ImageUploadResponse.class);

    }
}
