package com.tuwalike.wedding.models.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuestListItem {
    private String name;
    private String type;
    private String number;
}
