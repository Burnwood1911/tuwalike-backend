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
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String eventName;

    private String eventDate;

    private String location;

    private boolean security;

    private int guestCount;

    private int cardId;

    private String cardMessage;

    private String deliveryMethod;

    private String deliveryValue;

}
