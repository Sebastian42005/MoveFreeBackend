package com.example.movefree.database.feedback;

import com.example.movefree.database.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String message;

    @Enumerated(EnumType.STRING)
    private FeedbackType type;

    @ManyToOne
    private User user;

    public Feedback(String message, FeedbackType type, User user) {
        this.message = message;
        this.type = type;
        this.user = user;
    }
}
