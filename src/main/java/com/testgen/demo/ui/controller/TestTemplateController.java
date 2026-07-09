package com.testgen.demo.ui.controller;

import com.testgen.demo.core.model.TestData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestTemplateController {
    private static TestData testData;

    public static void setTestData(TestData data) {
        testData = data;
        System.out.println(getTestData());
    }
    public static TestData getTestData() { return testData; }

    @GetMapping("/test_template")
    public String insertTestData(Model model) {
        TestData data = getTestData();
        model.addAttribute("testData", data);
        model.addAttribute("questions", data.getQuestions());

        return "test_template";
    }
}
