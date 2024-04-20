package com.tuwalike.wedding.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.tuwalike.wedding.entity.Event;
import com.tuwalike.wedding.entity.Guest;
import com.tuwalike.wedding.models.GeneralResponse;
import com.tuwalike.wedding.models.message.MessageData;
import com.tuwalike.wedding.models.message.Messages;
import com.tuwalike.wedding.models.requests.CreateEventRequest;
import com.tuwalike.wedding.models.requests.DispatchRequest;
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

    private final SmsService smsService;

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

                return GeneralResponse.builder().statusCode(200).message("success")
                        .data(Map.of("guestName", guest.getName(), "inviteTyoe", guest.getGuestType())).build();
            } else {
                return GeneralResponse.builder().statusCode(500).message("Max Usage").build();
            }

        } else {
            return GeneralResponse.builder().statusCode(500).message("Guest not found").build();
        }

    }

    public GeneralResponse dispatch(DispatchRequest dispatchRequest) {

        Optional<Event> eventOptional = eventRepository.findById(dispatchRequest.getEventId());

        if (!eventOptional.isPresent()) {
            return GeneralResponse.builder().statusCode(500).message("failed").build();
        }

        List<Guest> guests = guestRepository.findAllByEventId(dispatchRequest.getEventId());

        if (!guests.isEmpty()) {

            List<MessageData> mList = guests.stream().map(g -> {

                String text = String.format(
                        "Habari %s Karibu kwenye sherehe ya binti yetu Dinah Kwilasa siku 30 Aprili 2024 bonyeza hapa %s kupata kadi yako au %s ukifika ukumbini. Karibu sana",
                        g.getName(), g.getFinalImage(), g.getQr());
                return MessageData.builder().from("SENDOFF").to(g.getPhoneNumber()).text(text).build();
            }).collect(Collectors.toList());

            Messages messages = new Messages();

            messages.setMessages(mList);
            messages.setReference(UUID.randomUUID().toString());

            smsService.send(messages);

        }

        return GeneralResponse.builder().statusCode(200).message("success").build();
    }

}
