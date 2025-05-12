package com.example.movefree.database.spot.image;

public class LowResPicture {
    private final byte[] picture;
    private final String contentType;

    public LowResPicture(byte[] picture, String contentType) {
        this.picture = picture;
        this.contentType = contentType;
    }

    public byte[] getPicture() {
        return picture;
    }

    public String getContentType() {
        return contentType;
    }
}
