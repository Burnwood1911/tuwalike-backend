package com.tuwalike.wedding.utils;

import org.springframework.stereotype.Service;

import com.tuwalike.wedding.entity.Card;
import com.tuwalike.wedding.entity.Guest;
import com.tuwalike.wedding.models.imageupload.ImageUploadResponse;
import com.tuwalike.wedding.service.FileUploader;
import com.tuwalike.wedding.service.ImageBBService;

import lombok.RequiredArgsConstructor;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

import javax.imageio.ImageIO;

@Service
@RequiredArgsConstructor
public class ImageUtil {

    private final ImageBBService imageBBService;

    private final FileUploader fileUploader;

    public String encode(String base64Qr, String inputPath, Card card, Guest guest) throws Exception {
        // Decode the base64 String of the QR code to a BufferedImage
        byte[] qrImageBytes = Base64.getDecoder().decode(base64Qr);
        ByteArrayInputStream baisQR = new ByteArrayInputStream(qrImageBytes);
        BufferedImage qrImage = ImageIO.read(baisQR);

        // Decode the base64 String of the input image to a BufferedImage
        byte[] inputImageBytes = Base64.getDecoder().decode(inputPath);
        // Path path = Paths.get(inputPath);
        // byte[] inputImageBytes = Files.readAllBytes(path);
        ByteArrayInputStream baisInput = new ByteArrayInputStream(inputImageBytes);
        BufferedImage inputImage = ImageIO.read(baisInput);

        // Resize the QR code with a white background
        BufferedImage qrImageResized = new BufferedImage(250, 250, BufferedImage.TYPE_INT_ARGB); // Use TYPE_INT_RGB to
                                                                                                 // support white
                                                                                                 // background
        Graphics2D g2d = qrImageResized.createGraphics();

        // Fill the background with white
        // g2d.setColor(Color.WHITE);
        // g2d.fillRect(0, 0, 200, 200);

        // Draw the resized QR code image
        Image resizedQrImage = qrImage.getScaledInstance(250, 250, Image.SCALE_SMOOTH);
        g2d.drawImage(resizedQrImage, 0, 0, null);
        g2d.dispose();

        // Calculate the position at the bottom left corner of the input image
        int x = 0;
        int y = inputImage.getHeight() - qrImageResized.getHeight();

        // Draw the resized QR code with a white background on the input image
        Graphics2D g2dInputImage = inputImage.createGraphics();
        g2dInputImage.drawImage(qrImageResized, x, y, null);

        // Add white text "SINGLE" at coordinates (100,200)
        g2dInputImage.setColor(Color.decode(card.getTypeColor()));

        // Assuming you've placed the font file in 'src/main/resources/fonts' and it's
        // named 'YourGoogleFont-Regular.ttf'
        Font myCustomFont = CustomFontUtil.loadFont("fonts/GreatVibes-Regular.ttf", 50f);
        g2dInputImage.setFont(myCustomFont);
        // g2dInputImage.setFont(new Font("Arial", Font.BOLD, 20)); // Set the font
        // here; you might need to adjust size
        g2dInputImage.drawString(StringUtil.capitalizeFirst(guest.getGuestType().toLowerCase()), card.getTypeX(),
                card.getTypeY());

        // draw name
        String fontName = "fonts/" + card.getFontName() + ".ttf";
        Font myCustomFontt = CustomFontUtil.loadFont(fontName, 70f);
        g2dInputImage.setFont(myCustomFontt);
        g2dInputImage.setColor(Color.decode(card.getNameColor()));
        g2dInputImage.drawString(guest.getName(), card.getNameX(), card.getNameY());

        g2dInputImage.dispose();

        // Convert the modified input image back to a base64 string
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(inputImage, "png", baos);
        byte[] imageBytes = baos.toByteArray();

        String unique = UUID.randomUUID().toString().split("-")[0];
        String filename = guest.getName().replaceAll(" ", "-") + unique + ".png";
        String url = fileUploader.upload(imageBytes, filename);

        // fileStorageService.storeFile(imageBytes, filename);
        // return fileStorageService.generateFileUrl(filename);

        // IMAGEBB SERIVCE

        // ImageUploadResponse imageUploadResponse =
        // imageBBService.upload(Base64.getEncoder().encodeToString(imageBytes));

        // return imageUploadResponse.getData().getImage().getUrl();
        return url;
    }

}