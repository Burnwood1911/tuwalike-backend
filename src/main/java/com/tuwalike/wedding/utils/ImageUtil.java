package com.tuwalike.wedding.utils;

import org.springframework.stereotype.Service;

import com.tuwalike.wedding.entity.Card;
import com.tuwalike.wedding.entity.Guest;
import com.tuwalike.wedding.models.NetworkResponse;
import com.tuwalike.wedding.service.FileUploader;

import lombok.RequiredArgsConstructor;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import javax.imageio.ImageIO;

@Service
@RequiredArgsConstructor
public class ImageUtil {

    private final FileUploader fileUploader;

    private final NetworkUtil networkUtil;

    public String encode(String base64Qr, String inputBytes, Card card, Guest guest) throws Exception {
        // Decode the base64 String of the QR code to a BufferedImage
        byte[] qrImageBytes = Base64.getDecoder().decode(base64Qr);
        ByteArrayInputStream baisQR = new ByteArrayInputStream(qrImageBytes);
        BufferedImage qrImage = ImageIO.read(baisQR);

        // Decode the base64 String of the input image to a BufferedImage
        byte[] inputImageBytes = Base64.getDecoder().decode(inputBytes);
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

        if (!guest.getGuestType().isEmpty()) {
            g2dInputImage.drawString(StringUtil.capitalizeFirst(guest.getGuestType().toLowerCase()),
                    card.getTypeX(),
                    card.getTypeY());
        }

        // draw name
        String fontName = "fonts/" + card.getFontName() + ".ttf";
        Font myCustomFontt = CustomFontUtil.loadFont(fontName, 70f);
        g2dInputImage.setFont(myCustomFontt);
        g2dInputImage.setColor(Color.decode(card.getNameColor()));

        String name = guest.getName();

        int axisStart = 160;
        int axisEnd = 930;
        int stringLength = name.length();
        int charWidth = 25;

        int stringStartPosition = calculateStringCenterPosition(axisStart, axisEnd, stringLength, charWidth);

        g2dInputImage.drawString(name, stringStartPosition, card.getNameY());

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

    public static int calculateStringCenterPosition(int axisStart, int axisEnd, int stringLength, int charWidth) {
        int axisWidth = axisEnd - axisStart; // Calculate the total width of the x-axis
        int axisMidpoint = axisStart + axisWidth / 2; // Find the midpoint of the x-axis

        int stringWidth = stringLength * charWidth; // Calculate the total width of the string
        int stringStart = axisMidpoint - stringWidth / 2; // Calculate the starting position of the string to center it

        return stringStart; // This is the x coordinate where the string should start to be centered
    }

    public byte[] download(String url) {

        NetworkResponse networkResponse = networkUtil.get(url, Map.of());

        byte[] imageBytes = networkResponse.getResponseBody().getBytes();

        return imageBytes;
    }

}