package com.example.movefree.file;

import com.example.movefree.portclass.Picture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class ImageReader {

    private ImageReader() {}
    public static byte[] readImageBytes(String imagePath) throws IOException {
        ClassPathResource resource = new ClassPathResource(imagePath);
        InputStream inputStream = resource.getInputStream();
        return StreamUtils.copyToByteArray(inputStream);
    }

    public static Picture getProfilePicture(boolean dark) {
        try {
            if (dark) {
                return new Picture(MediaType.IMAGE_PNG, readImageBytes("images/profile.png"));
            } else {
                return new Picture(MediaType.IMAGE_PNG, readImageBytes("images/profile_dark.png"));
            }
        }catch (IOException exception) {
            log.error("IOException: " + exception.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
