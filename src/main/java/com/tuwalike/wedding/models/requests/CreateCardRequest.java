package com.tuwalike.wedding.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCardRequest {

    private String image;

    private Integer price;

    private Integer nameX;

    private Integer nameY;

    private String nameColor;

    private Integer typeX;

    private Integer typeY;

    private String typeColor;

    private Integer categoryId;

    private String fontName;
}
