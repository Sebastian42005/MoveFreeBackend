package com.example.movefree.database.feedback;

public record FeedbackRequest(
    String message,
    FeedbackType type
) {}