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
public class ImageData {
    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("url_viewer")
    private String urlViewer;
    @SerializedName("url")
    private String url;
    @SerializedName("display_url")
    private String displayUrl;
    @SerializedName("width")
    private int width;
    @SerializedName("height")
    private int height;
    @SerializedName("size")
    private int size;
    @SerializedName("time")
    private int time;
    @SerializedName("expiration")
    private int expiration;
    @SerializedName("image")
    private Image image;
    @SerializedName("delete_url")
    private String deleteUrl;
}
