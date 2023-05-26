package com.example.movefree.service.user;

public record UserProfilePicture (
        byte[] content,
        String contentType
) {
    @Override
    public String toString() {
        return "Image";
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
