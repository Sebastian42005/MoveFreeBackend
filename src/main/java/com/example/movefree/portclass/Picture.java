package com.example.movefree.portclass;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public record Picture(
        MediaType contentType,
        byte[] content
) {
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Picture other)) return false;
        return Objects.equals(contentType, other.contentType) &&
                Arrays.equals(content, other.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contentType, Arrays.hashCode(content));
    }

    @Override
    public String toString() {
        return "ProfilePicture{" +
                "contentType=" + contentType +
                ", content=" + Arrays.toString(content) +
                '}';
    }

    public static boolean checkIfValidImage(MultipartFile multipartFile) {
        List<String> imageTypeList = List.of(MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE);
        return imageTypeList.contains(multipartFile.getContentType());
    }
}
