package com.tuwalike.wedding.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class QRCodeGenerator {

    // Helper method to trim the BitMatrix (reduce margin/whitespace)
    private static BitMatrix trimBitMatrix(BitMatrix matrix, int margin) {
        int[] rectangle = matrix.getEnclosingRectangle();
        if (rectangle == null)
            throw new IllegalArgumentException("BitMatrix is empty or margin is too large");

        int newWidth = rectangle[2] + margin * 2;
        int newHeight = rectangle[3] + margin * 2;

        BitMatrix newMatrix = new BitMatrix(newWidth, newHeight);
        for (int y = 0; y < newHeight - margin * 2; y++) {
            for (int x = 0; x < newWidth - margin * 2; x++) {
                if (matrix.get(rectangle[0] + x, rectangle[1] + y)) {
                    newMatrix.set(x + margin, y + margin);
                }
            }
        }
        return newMatrix;
    }

    public static String generateQRCodeImageAsBase64(String text, int width, int height)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        // Trim the BitMatrix to reduce margin
        BitMatrix trimmedMatrix = trimBitMatrix(bitMatrix, 5); // '4' is the margin size, adjust as needed

        // Convert trimmed BitMatrix to BufferedImage
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(trimmedMatrix);

        // Convert BufferedImage to ByteArrayOutputStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "PNG", baos);

        // Encode the ByteArrayOutputStream to Base64 string
        String base64String = Base64.getEncoder().encodeToString(baos.toByteArray());

        return base64String;
    }

    // public static String generateQRCodeImageAsBase64(String text, int width, int
    // height)
    // throws WriterException, IOException {
    // QRCodeWriter qrCodeWriter = new QRCodeWriter();
    // BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width,
    // height);

    // // Convert BitMatrix to BufferedImage
    // BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

    // // Convert BufferedImage to ByteArrayOutputStream
    // ByteArrayOutputStream baos = new ByteArrayOutputStream();
    // ImageIO.write(bufferedImage, "PNG", baos);

    // // Encode the ByteArrayOutputStream to Base64 string
    // String base64String = Base64.getEncoder().encodeToString(baos.toByteArray());

    // return base64String;
    // }

    // Example of how to call the function
    // public static void main(String[] args) {
    // try {
    // String text = "https://example.com"; // This is the text that you want to
    // encode into the QR code
    // int width = 200; // QR code image width
    // int height = 200; // QR code image height

    // String qrCodeBase64 = generateQRCodeImageAsBase64(text, width, height);
    // System.out.println("QR Code Base64: " + qrCodeBase64);
    // } catch (WriterException | IOException e) {
    // System.err.println("Error generating QR Code");
    // e.printStackTrace();
    // }
    // }
}