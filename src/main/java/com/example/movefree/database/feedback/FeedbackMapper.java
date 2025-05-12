package com.example.movefree.database.feedback;

import com.example.movefree.database.user.User;

import java.util.Optional;
import java.util.function.Function;

public class FeedbackMapper implements Function<Feedback, FeedbackResponse> {
    @Override
    public FeedbackResponse apply(Feedback feedback) {
        return new FeedbackResponse(
                feedback.getId(),
                feedback.getMessage(),
                feedback.getType(),
                Optional.ofNullable(feedback.getUser()).map(User::getUsername).orElse(null)
        );
    }
}
