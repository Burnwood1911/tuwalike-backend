package com.tuwalike.wedding.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDeliveryRequest {
    private int eventId;

    private String channel;

    private String value;
}
