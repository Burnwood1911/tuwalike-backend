package com.tuwalike.wedding.utils;

import org.springframework.stereotype.Service;

import com.tuwalike.wedding.entity.Card;
import com.tuwalike.wedding.entity.Guest;
import com.tuwalike.wedding.service.FileUploader;

import lombok.RequiredArgsConstructor;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.UUID;
import javax.imageio.ImageIO;

public class ImagTestingUtileUtil {

    public static int calculateStringCenterPosition(int axisStart, int axisEnd, int stringLength, int charWidth) {
        int axisWidth = axisEnd - axisStart; // Calculate the total width of the x-axis
        int axisMidpoint = axisStart + axisWidth / 2; // Find the midpoint of the x-axis

        int stringWidth = stringLength * charWidth; // Calculate the total width of the string
        int stringStart = axisMidpoint - stringWidth / 2; // Calculate the starting position of the string to center it

        return stringStart; // This is the x coordinate where the string should start to be centered
    }

    public static void main(String[] args) throws Exception {

        String qrk = QRCodeGenerator.generateQRCodeImageAsBase64("kapa", 250, 250);

        String filePatht = "uploads/text.txt"; // Assuming the file is in the current working directory
        String content = new String(Files.readAllBytes(Paths.get(filePatht)));

        // Decode the base64 String of the QR code to a BufferedImage
        byte[] qrImageBytes = Base64.getDecoder().decode(qrk);
        ByteArrayInputStream baisQR = new ByteArrayInputStream(qrImageBytes);
        BufferedImage qrImage = ImageIO.read(baisQR);

        // Decode the base64 String of the input image to a BufferedImage
        byte[] inputImageBytes = Base64.getDecoder().decode(content);
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
        g2dInputImage.setColor(Color.black);

        // Assuming you've placed the font file in 'src/main/resources/fonts' and it's
        // named 'YourGoogleFont-Regular.ttf'
        Font myCustomFont = CustomFontUtil.loadFont("fonts/GreatVibes-Regular.ttf", 60f);
        g2dInputImage.setFont(myCustomFont);
        // g2dInputImage.setFont(new Font("Arial", Font.BOLD, 20)); // Set the font
        // here; you might need to adjust size
        // g2dInputImage.drawString(StringUtil.capitalizeFirst(guest.getGuestType().toLowerCase()),
        // card.getTypeX(),
        // card.getTypeY());

        // draw name
        g2dInputImage.setFont(myCustomFont);
        g2dInputImage.setColor(Color.black);

        String name = "Mr & Mrs Makwaia";

        int axisStart = 160;
        int axisEnd = 930;
        int stringLength = name.length();
        int charWidth = 25;

        int stringStartPosition = calculateStringCenterPosition(axisStart, axisEnd, stringLength, charWidth);

        g2dInputImage.drawString(name, stringStartPosition, 610);

        g2dInputImage.dispose();

        // Convert the modified input image back to a base64 string
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(inputImage, "png", baos);
        byte[] imageBytes = baos.toByteArray();

        Path uploadsDir = Paths.get("uploads");
        Path filePath = uploadsDir.resolve("kappa.png");

        try {
            // Ensure the directory exists
            if (Files.notExists(uploadsDir)) {
                Files.createDirectories(uploadsDir);
            }

            // Writing the byte array to a file
            Files.write(filePath, imageBytes, StandardOpenOption.CREATE);

            System.out.println("File saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving PNG file: " + e.getMessage());
        }

    }

}