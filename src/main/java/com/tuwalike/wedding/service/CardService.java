package com.tuwalike.wedding.service;

import com.google.zxing.WriterException;
import com.tuwalike.wedding.entity.Card;
import com.tuwalike.wedding.entity.CardCategory;
import com.tuwalike.wedding.entity.Event;
import com.tuwalike.wedding.entity.Guest;
import com.tuwalike.wedding.models.GeneralResponse;
import com.tuwalike.wedding.models.imageupload.ImageUploadResponse;
import com.tuwalike.wedding.models.requests.CreateCardRequest;
import com.tuwalike.wedding.models.requests.GenerateCardRequest;
import com.tuwalike.wedding.repository.CardCategoryRepository;
import com.tuwalike.wedding.repository.CardRepository;
import com.tuwalike.wedding.repository.EventRepository;
import com.tuwalike.wedding.repository.GuestRepository;
import com.tuwalike.wedding.utils.ImageUtil;
import com.tuwalike.wedding.utils.QRCodeGenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import java.io.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardService {

    private final CardRepository cardRepository;

    private final CardCategoryRepository cardCategoryRepository;

    private final GuestRepository guestRepository;

    private final EventRepository eventRepository;

    private final ImageUtil imageUtil;

    private final ImageBBService imageBBService;

    private final FileUploader fileUploader;

    public GeneralResponse getCards() {

        return GeneralResponse.builder().statusCode(200).message("success").data(cardRepository.findAll()).build();
    }

    public GeneralResponse getCard(Integer id) {

        Optional<Card> card = cardRepository.findById(id);

        if (card.isPresent()) {
            return GeneralResponse.builder().statusCode(200).message("success").data(card.get()).build();
        }

        return GeneralResponse.builder().statusCode(500).message("card not found").build();

    }

    public GeneralResponse createCard(CreateCardRequest request) {

        try {

            // Decode Base64 string to bytes. Assume Base64 string might have data prefix
            // String base64Image = request.getImage();
            // if (base64Image.startsWith("data:image/png;base64,")) {
            // base64Image = base64Image.replace("data:image/png;base64,", "");
            // }
            // byte[] decodedImg =
            // Base64.getDecoder().decode(base64Image.getBytes(StandardCharsets.UTF_8));

            // The original file name could be dynamically generated or retrieved if needed
            // String originalFileName = "uploadedImage.png";

            // Use FileStorageService to store the image and generate the file name
            // fileStorageService.storeFile(decodedImg, originalFileName);
            // String fileDownloadUri =
            // fileStorageService.generateFileUrl(originalFileName);

            // ImageUploadResponse imageUploadResponse =
            // imageBBService.upload(request.getImage());

            String filename = UUID.randomUUID().toString().split("-")[0] + ".png";
            String durl = fileUploader.upload(Base64.getDecoder().decode(request.getImage()), filename);
            Card card = new Card();
            card.setImageBase(request.getImage());
            card.setImage(durl);
            card.setPrice(request.getPrice());
            card.setNameX(request.getNameX());
            card.setNameY(request.getNameY());
            card.setTypeX(request.getTypeX());
            card.setTypeY(request.getTypeY());
            card.setNameColor(request.getNameColor());
            card.setTypeColor(request.getTypeColor());
            card.setFontName(request.getFontName());

            Optional<CardCategory> cardCategory = cardCategoryRepository.findById(request.getCategoryId());

            if (cardCategory.isPresent()) {
                card.setCardCategory(cardCategory.get());
            } else {
                return GeneralResponse.builder().statusCode(500).message("Invalid card category").build();
            }

            cardRepository.save(card);

            return GeneralResponse.builder().statusCode(200).message("Success").build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return GeneralResponse.builder().statusCode(500).message("Error processing the image").build();
        }
    }

    public GeneralResponse deleteCard(Integer id) {

        cardRepository.deleteById(id);

        return GeneralResponse.builder().statusCode(200).message("success").build();
    }

    public GeneralResponse generate(GenerateCardRequest request) throws Exception {

        Optional<Event> event = eventRepository.findById(request.getEventId());

        if (event.isPresent()) {
            Optional<Card> card = cardRepository.findById(event.get().getCardId());

            if (card.isPresent()) {

                List<Guest> guests = guestRepository.findAllByEventId(event.get().getId());

                guests.forEach(g -> {

                    String qrImage = "";
                    try {
                        qrImage = QRCodeGenerator.generateQRCodeImageAsBase64(g.getQr(), 250, 250);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        String out = imageUtil.encode(qrImage, card.get().getImageBase(), card.get(), g);

                        g.setFinalImage(out);

                        guestRepository.save(g);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

            } else {
                return GeneralResponse.builder().statusCode(500).message("card not found").build();

            }
        } else {

            return GeneralResponse.builder().statusCode(500).message("event not found").build();

        }

        return GeneralResponse.builder().statusCode(200).message("success").build();
    }
}
