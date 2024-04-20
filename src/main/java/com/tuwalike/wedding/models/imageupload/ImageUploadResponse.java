package com.tuwalike.wedding.models.imageupload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageUploadResponse {
    @SerializedName("data")
    private ImageData data;
    @SerializedName("success")
    private boolean success;
    @SerializedName("status")
    private int status;
}
