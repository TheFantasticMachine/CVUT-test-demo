package com.testgen.demo.core.engine;

import com.testgen.demo.core.model.Category;
import com.testgen.demo.core.model.Question;
import com.testgen.demo.core.model.Subject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Test {

    private int questionAmount, uniqueCategoriesAmount, variants;
    private Subject subject;
    private ArrayList<Category> categories;

    public Test (int questionAmount, int uniqueCategoriesAmount, int variants, Subject subject, ArrayList<Category> categories) {
        this.questionAmount = questionAmount;
        this.uniqueCategoriesAmount = uniqueCategoriesAmount;
        this.variants = variants;
        this.subject = subject;
        this.categories = categories;
    }

    public void createTest() {
        Random random = new Random();

        for (int i = 0; i < variants; i++) {
            // select subjects - forced select
            HashMap<Category, Integer> selectedCategories = new HashMap<>();
            for (int j = 0; j < uniqueCategoriesAmount; j++) {
                // get random value
                Category selected = categories.get(random.nextInt(categories.size()));
                // assign
                selectedCategories.put(selected, 1);
                categories.remove(selected);
            }

            // add questions - give points
            for (int j = 0; j < questionAmount - uniqueCategoriesAmount; j++) {
                // get keys
                Category[] keys = selectedCategories.keySet().toArray(new Category[0]);
                // select random key and val
                Category selectedKey = keys[random.nextInt(keys.length)];
                Integer selectedVal = selectedCategories.get(selectedKey);
                // remove the selected key
                selectedCategories.replace(selectedKey, selectedVal, selectedVal++);
            }

            Question[] testQuestions;
        }
    }
}


class TestData {
    private String creator, subjectName;
    private LocalDateTime localDateTime;
    private int variant;
    private Question[] questions;

    TestData(String creator, String subjectName, LocalDateTime localDateTime, int variant, Question[] questions) {
        this.creator = creator;
        this.subjectName = subjectName;
        this.localDateTime = localDateTime;
        this.variant = variant;
        this.questions = questions;
    }
}