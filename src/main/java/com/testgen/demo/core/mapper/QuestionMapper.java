package com.testgen.demo.core.mapper;

import com.testgen.demo.core.dto.QuestionDto;
import com.testgen.demo.core.entity.QuestionEntity;

public class QuestionMapper {

    public static QuestionDto mapToQuestionDto(QuestionEntity question) {
        return new QuestionDto(
                question.getQuestionID(),
                question.getQuestionText(),
                question.getCorrectAnswer(),
                question.getOtherAnswer(),
                question.getCategoryID(),
                question.getStatus(),
                question.getSubjectID()
        );
    }

    public static QuestionEntity mapToQuestionEntity(QuestionDto questionDto) {
        return new QuestionEntity(
                questionDto.getQuestionID(),
                questionDto.getQuestionText(),
                questionDto.getCorrectAnswer(),
                questionDto.getOtherAnswer(),
                questionDto.getCategoryID(),
                questionDto.getStatus(),
                questionDto.getSubjectID()
        );
    }
}
