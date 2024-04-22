package com.tuwalike.wedding.service;

import com.tuwalike.wedding.entity.Card;
import com.tuwalike.wedding.entity.CardCategory;
import com.tuwalike.wedding.entity.Event;
import com.tuwalike.wedding.entity.Guest;
import com.tuwalike.wedding.models.GeneralResponse;
import com.tuwalike.wedding.models.requests.CreateCardRequest;
import com.tuwalike.wedding.models.requests.GenerateCardRequest;
import com.tuwalike.wedding.repository.CardCategoryRepository;
import com.tuwalike.wedding.repository.CardRepository;
import com.tuwalike.wedding.repository.EventRepository;
import com.tuwalike.wedding.repository.GuestRepository;
import com.tuwalike.wedding.utils.ImageUtil;
import com.tuwalike.wedding.utils.QRCodeGenerator;

import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;

    private final CardCategoryRepository cardCategoryRepository;

    private final GuestRepository guestRepository;

    private final EventRepository eventRepository;

    private final ImageUtil imageUtil;

    private final FileUploader fileUploader;

    private final Executor asyncExecutor;

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
                card.setCardCategory(cardCategory.get().getId());
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

    // public GeneralResponse generate(GenerateCardRequest request) {

    // Optional<Event> event = eventRepository.findById(request.getEventId());
    // if (!event.isPresent()) {
    // return GeneralResponse.builder().statusCode(500).message("event not
    // found").build();
    // }

    // Optional<Card> card = cardRepository.findById(event.get().getCardId());
    // if (!card.isPresent()) {
    // return GeneralResponse.builder().statusCode(500).message("card not
    // found").build();
    // }

    // List<Guest> guests = guestRepository.findAllByEventId(event.get().getId());

    // List<CompletableFuture<Void>> futures = guests.stream().filter(p ->
    // p.getFinalImage() == null)
    // .map(g -> CompletableFuture.runAsync(() -> {
    // try {
    // String qrImage = QRCodeGenerator.generateQRCodeImageAsBase64(g.getQr(), 250,
    // 250);
    // String out = imageUtil.encode(qrImage, card.get().getImageBase(), card.get(),
    // g);
    // g.setFinalImage(out);
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }, asyncExecutor)).collect(Collectors.toList());

    // // Wait for all async tasks to complete
    // CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

    // // Now save all guests at once if your JPA provider supports it
    // guestRepository.saveAll(guests);

    // return GeneralResponse.builder().statusCode(200).message("success").build();
    // }

    public GeneralResponse generate(GenerateCardRequest request) {
        Optional<Event> event = eventRepository.findById(request.getEventId());
        if (!event.isPresent()) {
            return GeneralResponse.builder().statusCode(500).message("Event not found").build();
        }

        Optional<Card> card = cardRepository.findById(event.get().getCardId());
        if (!card.isPresent()) {
            return GeneralResponse.builder().statusCode(500).message("Card not found").build();
        }

        List<Guest> guests = guestRepository.findAllByEventId(event.get().getId());

        // Call processGuestList asynchronously
        processGuestList(guests, card.get());

        // Return response immediately
        return GeneralResponse.builder().statusCode(202).message("Processing started").build();
    }

    @Async("asyncExecutor")
    public void processGuestList(List<Guest> guests, Card card) {
        List<CompletableFuture<Void>> futures = guests.stream().filter(p -> p.getFinalImage() == null)
                .map(g -> CompletableFuture.runAsync(() -> {
                    try {
                        String qrImage = QRCodeGenerator.generateQRCodeImageAsBase64(g.getQr(), 250, 250);
                        String out = imageUtil.encode(qrImage, card.getImageBase(), card, g);
                        g.setFinalImage(out);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, asyncExecutor)).collect(Collectors.toList());

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenRun(() -> guestRepository.saveAll(guests));
    }

    public GeneralResponse generateOld(GenerateCardRequest request) throws Exception {

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
