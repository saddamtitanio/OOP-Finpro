package com.game.backend.service;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Random;

@Service
public class DeathMessageService {

    private static final List<String> MESSAGES = List.of(
            "You let a zombie hug you.",
            "Skill issue tbh.",
            "You became the snack.",
            "Zombies stays winning.",
            "You zigged when you should’ve zagged.",
            "Bro forgot it bites.",
            "GG WP",
            "Social distancing is reccomended",
            "Bro is feeding",
            "Should've dodged that"
    );

    public String random() {
        return MESSAGES.get(new Random().nextInt(MESSAGES.size()));
    }
}
