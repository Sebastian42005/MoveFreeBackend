package com.example.movefree.database.feedback;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FeedbackType {
    BUG("Fehler"),
    FEATURE_REQUEST("Feature-Wunsch"),
    IMPROVEMENT("Verbesserung"),
    PRAISE("Lob"),
    QUESTION("Frage"),
    OTHER("Sonstiges");

    private final String label;
}

