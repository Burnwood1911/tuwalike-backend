package com.tuwalike.wedding.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tuwalike.wedding.models.GeneralResponse;
import com.tuwalike.wedding.models.requests.CreateEventRequest;
import com.tuwalike.wedding.models.requests.DispatchRequest;
import com.tuwalike.wedding.models.requests.ScanRequest;
import com.tuwalike.wedding.models.requests.UpdateDeliveryRequest;
import com.tuwalike.wedding.service.EventService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventService eventService;

    @PostMapping("create")
    public ResponseEntity<GeneralResponse> createEvent(@RequestBody CreateEventRequest request) {

        GeneralResponse result = eventService.createEvent(request);

        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @PostMapping("update-delivery")
    public ResponseEntity<GeneralResponse> updateDelivery(@RequestBody UpdateDeliveryRequest request) {

        GeneralResponse result = eventService.updateDelivery(request);

        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @PostMapping("scan")
    public ResponseEntity<GeneralResponse> scan(@RequestBody ScanRequest request) {

        GeneralResponse result = eventService.scan(request);

        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @PostMapping("dispatch")
    public ResponseEntity<GeneralResponse> scan(@RequestBody DispatchRequest request) {

        GeneralResponse result = eventService.dispatch(request);

        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }
}
