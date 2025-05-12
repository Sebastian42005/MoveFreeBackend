package com.example.movefree.helper;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageUtil {

    public static byte[] pixelateImage(MultipartFile file, int pixelSize) throws IOException {
        // Bild einlesen
        BufferedImage originalImage = ImageIO.read(file.getInputStream());

        // Orientierung korrigieren
        originalImage = correctOrientation(file, originalImage);

        // Breite & Höhe nach der Rotation holen
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        // Neues verpixeltes Bild vorbereiten
        BufferedImage pixelatedImage = new BufferedImage(width, height, originalImage.getType());
        Graphics2D graphics = pixelatedImage.createGraphics();

        // Verpixeln: Blöcke zeichnen
        for (int y = 0; y < height; y += pixelSize) {
            for (int x = 0; x < width; x += pixelSize) {
                int pixelColor = originalImage.getRGB(x, y);
                graphics.setColor(new Color(pixelColor));
                graphics.fillRect(x, y, pixelSize, pixelSize);
            }
        }

        graphics.dispose();

        // Bild in Byte-Array umwandeln
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(pixelatedImage, "jpg", outputStream);
        return outputStream.toByteArray();
    }

    public static BufferedImage correctOrientation(MultipartFile file, BufferedImage image) {
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file.getInputStream());
            ExifIFD0Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            if (directory != null && directory.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
                int orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);

                // Neue Größe bestimmen, falls nötig
                int width = image.getWidth();
                int height = image.getHeight();

                AffineTransform transform = new AffineTransform();
                switch (orientation) {
                    case 6: // 90° CW
                        transform.translate(height, 0);
                        transform.rotate(Math.toRadians(90));
                        BufferedImage rotated90 = new BufferedImage(height, width, image.getType());
                        new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR).filter(image, rotated90);
                        return rotated90;

                    case 3: // 180°
                        transform.translate(width, height);
                        transform.rotate(Math.toRadians(180));
                        BufferedImage rotated180 = new BufferedImage(width, height, image.getType());
                        new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR).filter(image, rotated180);
                        return rotated180;

                    case 8: // 90° CCW
                        transform.translate(0, width);
                        transform.rotate(Math.toRadians(270));
                        BufferedImage rotated270 = new BufferedImage(height, width, image.getType());
                        new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR).filter(image, rotated270);
                        return rotated270;

                    default:
                        return image; // Keine Drehung nötig
                }
            }
        } catch (Exception e) {
            // Kein EXIF oder fehlerhafte Daten → ignorieren
        }

        return image;
    }
}
