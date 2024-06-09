package com.javarush.quest.util;

import com.javarush.quest.entity.QuestionAnswer;
import lombok.Getter;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
public class QuestionLoader {
    private static final Logger LOGGER = Logger.getLogger(QuestionLoader.class.getName());
    private final List<QuestionAnswer> questions;

    public QuestionLoader(String fileName) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found: " + fileName);
            }
            LOGGER.log(Level.INFO, "Loading questions from file: " + fileName);
            Constructor constructor = new Constructor(QuestionsWrapper.class, new LoaderOptions());
            Yaml yaml = new Yaml(constructor);
            QuestionsWrapper wrapper = yaml.load(inputStream);
            questions = wrapper.getQuestions();

            if (questions == null || questions.isEmpty()) {
                LOGGER.log(Level.WARNING, "Questions list is empty");
            } else {
                LOGGER.log(Level.INFO, "Questions loaded successfully. Total questions: " + questions.size());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load questions from file: " + fileName, e);
            e.printStackTrace();
            throw new RuntimeException("Failed to load questions from file: " + fileName, e);
        }
    }

}
