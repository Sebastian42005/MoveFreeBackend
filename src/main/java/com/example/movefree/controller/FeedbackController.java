package com.example.movefree.controller;

import com.example.movefree.database.feedback.Feedback;
import com.example.movefree.database.feedback.FeedbackRequest;
import com.example.movefree.database.feedback.FeedbackResponse;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.service.feedback.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<FeedbackResponse> saveFeedback(@RequestBody FeedbackRequest feedback, Principal principal) throws IdNotFoundException {
        return ResponseEntity.ok(feedbackService.saveFeedback(feedback, principal));
    }

    @GetMapping
    public ResponseEntity<List<FeedbackResponse>> getAllFeedback() {
        return ResponseEntity.ok(feedbackService.getAllFeedback());
    }
}
