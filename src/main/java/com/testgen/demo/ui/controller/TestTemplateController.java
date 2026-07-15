package com.testgen.demo.ui.controller;

import com.testgen.demo.core.model.TestData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
public class TestTemplateController {
    private static final Map<Integer, TestData> testData = new HashMap<>();

    public static void setTestData(int variant, TestData data) {
        testData.put(variant, data);
    }

    public static Map<Integer, TestData> getTestData() {
        return testData;
    }

    @GetMapping("/test_template")
    public String insertTestData(Model model, @RequestParam(defaultValue = "1") int variant) {

        TestData data = this.getTestData().get(variant);

        if (data == null) {
            return "redirect:/?error=VariantNotFound";
        }

        model.addAttribute("testData", data);
        model.addAttribute("questions", data.getQuestions());

        return "test_template";
    }
}
