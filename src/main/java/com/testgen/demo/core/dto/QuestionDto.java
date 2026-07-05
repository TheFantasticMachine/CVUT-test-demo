package com.testgen.demo.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto {
    private Integer questionID;
    private String questionText;
    private String correctAnswer;
    private String otherAnswer;
    private int categoryID;
    private List<String> status;
    private Integer subjectID;
}
