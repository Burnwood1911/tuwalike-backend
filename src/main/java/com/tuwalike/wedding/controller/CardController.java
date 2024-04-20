package com.tuwalike.wedding.controller;

import com.tuwalike.wedding.models.GeneralResponse;
import com.tuwalike.wedding.models.requests.CreateCardRequest;
import com.tuwalike.wedding.models.requests.GenerateCardRequest;
import com.tuwalike.wedding.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cards")
public class CardController {

    private final CardService cardService;

    @GetMapping("get-cards")
    public ResponseEntity<GeneralResponse> getCards() {

        GeneralResponse result = cardService.getCards();

        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping("get-card/{id}")
    public ResponseEntity<GeneralResponse> getCard(@PathVariable Integer id) {
        GeneralResponse result = cardService.getCard(id);

        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @PostMapping("create-card")
    public ResponseEntity<GeneralResponse> createCard(@RequestBody CreateCardRequest request) {

        GeneralResponse result = cardService.createCard(request);

        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping("delete-card/{id}")
    public ResponseEntity<GeneralResponse> deleteCard(@PathVariable Integer id) {

        GeneralResponse result = cardService.deleteCard(id);

        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @PostMapping("generate")
    public ResponseEntity<GeneralResponse> generate(@RequestBody GenerateCardRequest request) throws Exception {

        GeneralResponse result = cardService.generate(request);

        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }
}
