package com.tuwalike.wedding.service;

import com.tuwalike.wedding.entity.CardCategory;
import com.tuwalike.wedding.models.GeneralResponse;
import com.tuwalike.wedding.models.requests.CreateCategoryRequest;
import com.tuwalike.wedding.repository.CardCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardCategoryService {

    private final CardCategoryRepository cardCategoryRepository;

    public GeneralResponse getCategories() {

        return GeneralResponse.builder().statusCode(200).message("success").data(cardCategoryRepository.findAll())
                .build();
    }

    public GeneralResponse getCategory(Integer id) {

        Optional<CardCategory> cardCategory = cardCategoryRepository.findById(id);

        if (cardCategory.isPresent()) {
            return GeneralResponse.builder().statusCode(200).message("success").data(cardCategory.get()).build();
        }

        return GeneralResponse.builder().statusCode(500).message("card category not found").build();

    }

    public GeneralResponse createCategory(CreateCategoryRequest request) {

        CardCategory cardCategory = new CardCategory();
        cardCategory.setCategoryName(request.getCategoryName());

        cardCategoryRepository.save(cardCategory);

        return GeneralResponse.builder().statusCode(200).message("success").build();
    }

    public GeneralResponse deleteCategory(Integer id) {

        cardCategoryRepository.deleteById(id);

        return GeneralResponse.builder().statusCode(200).message("success").build();
    }
}
