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
public class Image {
    @SerializedName("filename")
    private String filename;
    @SerializedName("name")
    private String name;
    @SerializedName("mime")
    private String mime;
    @SerializedName("extension")
    private String extension;
    @SerializedName("url")
    private String url;
}
