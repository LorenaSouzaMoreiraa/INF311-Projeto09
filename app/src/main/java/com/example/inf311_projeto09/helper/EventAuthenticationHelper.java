package com.example.inf311_projeto09.helper;

import android.graphics.Bitmap;

import com.example.inf311_projeto09.model.Event;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

public class EventAuthenticationHelper {

    private static final Random RANDOM = new Random();

    private EventAuthenticationHelper() {
        //
    }

    public static String generateCheckCode(final Event.EventVerificationMethod verificationMethod) {
        if (verificationMethod == Event.EventVerificationMethod.QR_CODE) {
            return generateQRCodeText();
        } else {
            return generate4DigitCode();
        }
    }

    public static String generateQRCodeText() {
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            sb.append(chars.charAt(RANDOM.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public static String generate4DigitCode() {
        final int code = RANDOM.nextInt(10000);
        return String.format("%04d", code);
    }

    public static Bitmap generateQRCode(final String content, final int size) throws WriterException {
        final Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.MARGIN, 1);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        final MultiFormatWriter writer = new MultiFormatWriter();
        final BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, size, size, hints);

        final int width = bitMatrix.getWidth();
        final int height = bitMatrix.getHeight();
        final Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bmp.setPixel(x, y, bitMatrix.get(x, y) ? android.graphics.Color.BLACK : android.graphics.Color.WHITE);
            }
        }

        return bmp;
    }
}
