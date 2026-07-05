package com.testgen.demo.core.service.impl;

import com.testgen.demo.core.dto.QuestionDto;
import com.testgen.demo.core.entity.QuestionEntity;
import com.testgen.demo.core.mapper.QuestionMapper;
import com.testgen.demo.core.repository.QuestionRepository;
import com.testgen.demo.core.service.QuestionService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private QuestionRepository questionRepository;
    @Override
    public QuestionDto createQuestion(QuestionDto questionDto) {
        QuestionEntity question = QuestionMapper.mapToQuestionEntity(questionDto);
        QuestionEntity savedQuestion = questionRepository.save(question);
        return QuestionMapper.mapToQuestionDto(savedQuestion);
    }
}
