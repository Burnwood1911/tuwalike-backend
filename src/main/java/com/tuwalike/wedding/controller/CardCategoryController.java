package com.tuwalike.wedding.controller;


import com.tuwalike.wedding.models.GeneralResponse;
import com.tuwalike.wedding.models.requests.CreateCategoryRequest;
import com.tuwalike.wedding.service.CardCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/card-category")
public class CardCategoryController {

    private final CardCategoryService cardCategoryService;


    @GetMapping("get-categories")
    public ResponseEntity<GeneralResponse> getCards() {

        GeneralResponse result = cardCategoryService.getCategories();

        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping("get-category/{id}")
    public ResponseEntity<GeneralResponse> getCard(@PathVariable Integer id) {

        GeneralResponse result = cardCategoryService.getCategory(id);

        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @PostMapping("create-category")
    public ResponseEntity<GeneralResponse> createCard(@RequestBody CreateCategoryRequest request) {

        GeneralResponse result = cardCategoryService.createCategory(request);

        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping("delete-category/{id}")
    public ResponseEntity<GeneralResponse> deleteCard(@PathVariable Integer id) {

        GeneralResponse result = cardCategoryService.deleteCategory(id);

        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

}
