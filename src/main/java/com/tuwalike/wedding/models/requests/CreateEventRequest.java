package com.tuwalike.wedding.models.requests;

import java.util.List;

import com.tuwalike.wedding.models.common.GuestListItem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEventRequest {

    private String eventDate;

    private String eventName;

    private String eventLocation;

    private boolean withSecurity;

    private String cardMessage;

    private int guestCount;

    private int cardId;

    private List<GuestListItem> guestList;

}
