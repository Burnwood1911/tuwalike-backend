package com.tuwalike.wedding.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tuwalike.wedding.models.GeneralResponse;
import com.tuwalike.wedding.service.GuestService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/guests")
public class GuestController {

    private final GuestService guestService;

    @GetMapping("get-guest/{id}")
    public ResponseEntity<GeneralResponse> getGuest(@PathVariable Integer id) {
        GeneralResponse result = guestService.getGuest(id);

        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

}
