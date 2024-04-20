package com.tuwalike.wedding.models.message;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Messages {

    @SerializedName("messages")
    private List<MessageData> messages;
    @SerializedName("reference")
    private String reference;
}
