package com.tuwalike.wedding.models.message;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageData {
    @SerializedName("from")
    private String from;
    @SerializedName("to")
    private String to;
    @SerializedName("text")
    private String text;
}
