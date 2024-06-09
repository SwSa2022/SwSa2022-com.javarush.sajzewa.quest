package com.javarush.quest.entity;

import lombok.Data;

import java.util.List;
@Data
public class QuestionAnswer {
    private int id;
    private String question;
    private List<String> answers;
    private String correct;

    public QuestionAnswer(String question, List<String> answers, String correct) {
    }

    public QuestionAnswer(String question, int id, List<String> answers, String correct) {
        this.question = question;
        this.id = id;
        this.answers = answers;
        this.correct = correct;
    }

    public QuestionAnswer() {
    }
}

