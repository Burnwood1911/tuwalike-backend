package com.tuwalike.wedding.service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.tuwalike.wedding.entity.Event;
import com.tuwalike.wedding.entity.Guest;
import com.tuwalike.wedding.models.GeneralResponse;
import com.tuwalike.wedding.models.requests.CreateEventRequest;
import com.tuwalike.wedding.models.requests.ScanRequest;
import com.tuwalike.wedding.models.requests.UpdateDeliveryRequest;
import com.tuwalike.wedding.models.responses.CreateEventResponse;
import com.tuwalike.wedding.repository.EventRepository;
import com.tuwalike.wedding.repository.GuestRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    private final GuestRepository guestRepository;

    public GeneralResponse createEvent(CreateEventRequest createEventRequest) {

        Event event = new Event();

        event.setEventName(createEventRequest.getEventName());
        event.setCardMessage(createEventRequest.getCardMessage());
        event.setEventDate(createEventRequest.getEventDate());
        event.setGuestCount(createEventRequest.getGuestCount());
        event.setSecurity(createEventRequest.isWithSecurity());
        event.setLocation(createEventRequest.getEventLocation());
        event.setCardId(createEventRequest.getCardId());

        Event savedEvent = eventRepository.save(event);

        createEventRequest.getGuestList().forEach(g -> {

            Guest guest = new Guest();

            guest.setEventId(savedEvent.getId());
            guest.setGuestType(g.getType());
            guest.setName(g.getName());
            guest.setPhoneNumber(g.getNumber());
            if (Objects.equals(g.getType().toUpperCase(), "SINGLE")) {
                guest.setUses(1);
            } else {
                guest.setUses(2);
            }

            guest.setQr(UUID.randomUUID().toString().split("-")[0]);

            guestRepository.save(guest);
        });

        CreateEventResponse createEventResponse = new CreateEventResponse();
        createEventResponse.setEventId(savedEvent.getId());

        return GeneralResponse.builder().statusCode(200).message("success").data(createEventResponse).build();

    }

    public GeneralResponse updateDelivery(UpdateDeliveryRequest updateDeliveryRequest) {

        Optional<Event> eventOptional = eventRepository.findById(updateDeliveryRequest.getEventId());

        if (!eventOptional.isPresent()) {
            return GeneralResponse.builder().statusCode(500).message("failed").build();
        }

        Event event = eventOptional.get();
        event.setDeliveryMethod(updateDeliveryRequest.getChannel());
        event.setDeliveryValue(updateDeliveryRequest.getValue());

        eventRepository.save(event);

        return GeneralResponse.builder().statusCode(200).message("success").build();
    }

    public GeneralResponse scan(ScanRequest scanRequest) {

        Optional<Guest> optional = guestRepository.findByQr(scanRequest.getQr());

        if (optional.isPresent()) {

            Guest guest = optional.get();

            if (guest.getUses() != 0) {
                guest.setUses(guest.getUses() - 1);
                guestRepository.save(guest);

                return GeneralResponse.builder().statusCode(200).message("success").build();
            } else {
                return GeneralResponse.builder().statusCode(500).message("Max Usage").build();
            }

        } else {
            return GeneralResponse.builder().statusCode(500).message("Guest not found").build();
        }

    }

}
