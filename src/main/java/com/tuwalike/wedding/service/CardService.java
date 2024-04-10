package com.tuwalike.wedding.service;

import com.tuwalike.wedding.entity.Card;
import com.tuwalike.wedding.entity.CardCategory;
import com.tuwalike.wedding.models.GeneralResponse;
import com.tuwalike.wedding.models.requests.CreateCardRequest;
import com.tuwalike.wedding.repository.CardCategoryRepository;
import com.tuwalike.wedding.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;

    private final CardCategoryRepository cardCategoryRepository;

    public GeneralResponse getCards(){

        return GeneralResponse.builder().statusCode(200).message("success").data(cardRepository.findAll()).build();
    }


    public GeneralResponse getCard(Integer id){


        Optional<Card> card = cardRepository.findById(id);

        if(card.isPresent()) {
            return GeneralResponse.builder().statusCode(200).message("success").data(card.get()).build();
        }

        return  GeneralResponse.builder().statusCode(500).message("card not found").build();

    }
    public GeneralResponse createCard(CreateCardRequest request){


        Card card = new Card();
        card.setImage(request.getImage());
        card.setPrice(request.getPrice());

        Optional<CardCategory> cardCategory = cardCategoryRepository.findById(request.getCategoryId());

        if(cardCategory.isPresent()) {
            card.setCardCategory(cardCategory.get());

        }else {
            return GeneralResponse.builder().statusCode(500).message("invalid card category").build();
        }

        cardRepository.save(card);

        return GeneralResponse.builder().statusCode(200).message("success").build();
    }
    public GeneralResponse deleteCard(Integer id){


        cardRepository.deleteById(id);

        return GeneralResponse.builder().statusCode(200).message("success").build();
    }
}
