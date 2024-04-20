package com.tuwalike.wedding.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String image;

    @Lob
    private String imageBase;

    private Integer price;

    private Integer nameX;

    private Integer nameY;

    private String nameColor;

    private Integer typeX;

    private Integer typeY;

    private String typeColor;

    private String fontName;

    @OneToOne(cascade = CascadeType.ALL)
    private CardCategory cardCategory;

}
