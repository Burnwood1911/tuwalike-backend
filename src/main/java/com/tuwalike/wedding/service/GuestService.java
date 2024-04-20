package com.tuwalike.wedding.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tuwalike.wedding.entity.Guest;
import com.tuwalike.wedding.models.GeneralResponse;
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
}
