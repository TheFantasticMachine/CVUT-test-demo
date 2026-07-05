package com.testgen.demo.core.controller;

import com.testgen.demo.core.dto.QuestionDto;
import com.testgen.demo.core.service.QuestionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private QuestionService questionService;

    // build add question Rest API
    @PostMapping
    public ResponseEntity<QuestionDto> createQuestion(@RequestBody QuestionDto questionDto) {
        QuestionDto savedQuestion = questionService.createQuestion(questionDto);
        return new ResponseEntity<>(savedQuestion, HttpStatus.CREATED);
    }
}
