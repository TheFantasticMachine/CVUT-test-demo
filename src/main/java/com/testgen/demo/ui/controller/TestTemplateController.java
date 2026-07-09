package com.testgen.demo.ui.controller;

import com.testgen.demo.core.model.TestData;
import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestTemplateController {

    @GetMapping("/test_template")
    public String insertTestData(Model model, TestData data) {
        model.addAttribute("testData", data);
        model.addAttribute("questions", data.getQuestions());

        return "test_template";
    }
}
