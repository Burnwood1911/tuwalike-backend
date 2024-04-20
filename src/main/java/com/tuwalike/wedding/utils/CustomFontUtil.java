package com.tuwalike.wedding.utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

public class CustomFontUtil {

    public static Font loadFont(String path, float size) {
        try {
            InputStream is = CustomFontUtil.class.getClassLoader().getResourceAsStream(path);
            if (is == null) {
                throw new IllegalArgumentException("Font not found: " + path);
            }
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            return font.deriveFont(size);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return new Font("Arial", Font.PLAIN, (int) size); // Fallback font
        }
    }
}