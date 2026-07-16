package com.testgen.demo.ui.controller.api;

import com.testgen.demo.core.engine.DatabaseLoader;
import com.testgen.demo.core.model.Question;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Service
public class QuestionService {

    private List<Question> questionList;

    public QuestionService() {

        // get all questions via DB
        try {
            Connection connection = DatabaseLoader.getConnector();
            String sql = "select * from questions";
            Statement statement = connection.createStatement();
            ResultSet rawQuestions = statement.executeQuery(sql);

            while (rawQuestions.next()) {
                Question question = new Question();

                question.setQuestionID(rawQuestions.getInt("questionID"));
                question.setQuestionText(rawQuestions.getString("questionText"));

                String wrongString = rawQuestions.getString("otherAnswer");
                ArrayList<String> wrongAnswers = new ArrayList<>();
                if (wrongString != null && !wrongString.isEmpty()) {
                    for (String item : wrongString.split("\\|")) {
                        wrongAnswers.add(item.trim());
                    }
                }
                question.setWrong(wrongAnswers);
                question.setCorrect(rawQuestions.getString("correctAnswer"));
                questionList.add(question);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Question> getQuestion(Integer id) {
        Optional optional = Optional.empty();
        for (Question question: questionList) {
            if (id == question.getQuestionID()) {
                optional = Optional.of(question);
                return optional;
            }
        }
        return optional;
    }
}
