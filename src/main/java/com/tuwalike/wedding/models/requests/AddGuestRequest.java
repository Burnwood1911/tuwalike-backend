package com.tuwalike.wedding.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddGuestRequest {
    private int eventId;

    private String name;

    private String type;

    private String number;
}
