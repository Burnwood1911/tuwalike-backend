package com.tuwalike.wedding.service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.tuwalike.wedding.entity.Guest;
import com.tuwalike.wedding.models.GeneralResponse;
import com.tuwalike.wedding.models.requests.AddGuestRequest;
import com.tuwalike.wedding.repository.GuestRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GuestService {

    private final GuestRepository guestRepository;

    public GeneralResponse getGuest(Integer id) {

        Optional<Guest> guest = guestRepository.findById(id);

        if (guest.isPresent()) {
            return GeneralResponse.builder().statusCode(200).message("success").data(guest.get()).build();
        }

        return GeneralResponse.builder().statusCode(500).message("guest not found").build();

    }

    public GeneralResponse addGuest(AddGuestRequest request) {

        Guest guest = new Guest();

        guest.setName(request.getName());

        guest.setGuestType(request.getType());

        guest.setPhoneNumber(request.getNumber());

        guest.setEventId(request.getEventId());

        if (Objects.equals(request.getType().toUpperCase(), "SINGLE")) {
            guest.setUses(1);
        } else {
            guest.setUses(2);
        }

        guest.setQr(UUID.randomUUID().toString().split("-")[0]);

        guestRepository.save(guest);

        return GeneralResponse.builder().statusCode(200).message("success").data(guest).build();

    }
}
