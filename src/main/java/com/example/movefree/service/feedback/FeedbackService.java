package com.example.movefree.service.feedback;

import com.example.movefree.database.feedback.*;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final UserService userService;
    private final FeedbackMapper feedbackMapper = new FeedbackMapper();

    @Transactional
    public FeedbackResponse saveFeedback(FeedbackRequest feedback, Principal principal) throws IdNotFoundException {
        return feedbackMapper.apply(feedbackRepository.save(
                new Feedback(feedback.message(), feedback.type(), userService.getUser(principal))));
    }

    public List<FeedbackResponse> getAllFeedback() {
        return feedbackRepository.findAll().stream().map(feedbackMapper).toList();
    }
}
