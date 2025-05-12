package com.example.movefree.database.feedback;

public record FeedbackResponse(
    Long id,
    String message,
    FeedbackType type,
    String username
) {}