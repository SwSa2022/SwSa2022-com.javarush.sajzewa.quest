package com.javarush.quest.util;

import com.javarush.quest.entity.QuestionAnswer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionLoaderTest {

    @Test
    public void testQuestionLoading() {
        String fileName = "questions_answer.yml";

        QuestionLoader questionLoader = new QuestionLoader(fileName);

        List<QuestionAnswer> questions = questionLoader.getQuestions();

        assertNotNull(questions);
        assertFalse(questions.isEmpty());

        for (QuestionAnswer question : questions) {
            assertNotNull(question.getQuestion());
            assertFalse(question.getQuestion().isEmpty());

            assertNotNull(question.getAnswers());
            assertFalse(question.getAnswers().isEmpty());
        }

        assertEquals(5, questions.size());
    }
}
