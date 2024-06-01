package com.tuwalike.wedding.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import org.springframework.stereotype.Component;

@Component
public class ImageDownloader {

    // Method to download the image and return as Base64 string
    public String downloadAndConvertToBase64(String urlString) {
        try {
            // Create URL object
            URL url = new URL(urlString);

            // Open URL connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Check if the response code is 200 (HTTP OK)
            int responseCode = connection.getResponseCode();
            System.out.println("HTTP Response Code: " + responseCode);

            if (responseCode != HttpURLConnection.HTTP_OK) {
                System.err.println("Failed to download image. Response Code: " + responseCode);
                return null;
            }

            // Get input stream from connection
            InputStream inputStream = connection.getInputStream();

            // Convert input stream to byte array
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }

            byte[] imageBytes = byteArrayOutputStream.toByteArray();

            // Close streams
            inputStream.close();
            byteArrayOutputStream.close();

            // Encode bytes to Base64
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            return base64Image;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // public static void main(String[] args) {
    // ImageDownloader downloader = new ImageDownloader();

    // String imageUrl = "https://minio.alexrossi.xyz/tualike/abubakar.png";
    // String base64Image = downloader.downloadAndConvertToBase64(imageUrl);

    // if (base64Image != null) {
    // System.out.println("Base64 Encoded Image: " + base64Image);
    // } else {
    // System.err.println("Failed to download and encode image.");
    // }
    // }
}
