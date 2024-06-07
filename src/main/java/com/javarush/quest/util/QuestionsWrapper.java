package com.javarush.quest.util;

import com.javarush.quest.entity.QuestionAnswer;
import lombok.Data;


import java.util.List;
@Data
public class QuestionsWrapper {
        private List<QuestionAnswer> questions;
}
