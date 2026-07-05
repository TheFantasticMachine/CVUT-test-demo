package com.testgen.demo;

import com.testgen.demo.core.config.FileHandler;
import com.testgen.demo.core.model.*;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Test {

    private int questionAmount, uniqueCategoriesAmount, variants;
    private Subject subject;
    //private ArrayList<Category> categories;

    public Test (int questionAmount, int uniqueCategoriesAmount, int variants, Subject subject) {
        this.questionAmount = questionAmount;
        this.uniqueCategoriesAmount = uniqueCategoriesAmount;
        this.variants = variants;
        this.subject = subject;
    }

    public void createTest() {
        Random random = new Random();
        Map<Integer, String> tests = new HashMap<>();

        for (int testVariant = 1; testVariant <= this.variants; testVariant++) {
            System.out.println( "\n...creating a test variant from " + subject.getSubjectName());
            // -- pick categories from the given subject --
            List<Category> tempCategories = new ArrayList<>();
            for (Category category : subject.getCategories()) {
               tempCategories.add(category);
            }
            Map<Category, Integer> pickedCategories = new HashMap<>();
            System.out.println("-- Categories: ");
            tempCategories.forEach(category -> System.out.println(" - "  + category.getCategoryName()));
            System.out.println("-- Picked:");

            // -- random picks
            for (int i = 0; i <  this.uniqueCategoriesAmount; i++) {
                Category picked = tempCategories.get(random.nextInt(tempCategories.size()));
                pickedCategories.put(picked, 1);
                tempCategories.remove(picked);
            }

            pickedCategories.forEach(((category, integer) -> System.out.println(" - " + category.getCategoryName())));


            // -- give question points --
            for (int i = 0; i < questionAmount - pickedCategories.size(); i++) {
                // -- pick
                Set<Category> keySet = pickedCategories.keySet();
                Category[] keys = keySet.toArray(new Category[keySet.size()]);
                Category pickedCategory = keys[random.nextInt(keySet.size())];
                Integer pickedInt = pickedCategories.get(pickedCategory) + 1;
                if (pickedInt >= pickedCategory.getQuestions().size()) {
                    i--;
                    continue;
                }
                else {
                    // -- update
                    pickedCategories.remove(pickedCategory);
                    pickedCategories.put(pickedCategory, pickedInt);
                }
            }
            // -- show in console
            System.out.println("--- < Questions ready > ---");
            pickedCategories.forEach((category, integer) -> {
                System.out.println("| Category: " + category.getCategoryName() + " | INT: " + integer);
            });

            // -- give questions --
            List<TestQuestion> questions = new ArrayList<>();
            List<String> categoriesNames = new ArrayList<>();

            for (Category category: pickedCategories.keySet()) {
                Integer questionAmount = pickedCategories.get(category);
                List<Question> questionList = category.getUniqueQuestions(questionAmount);
                for (Question question : questionList) {
                    // -- create list
                    List<String> answers = question.giveAnswers();
                    // -- create class
                    TestQuestion testQuestion = new TestQuestion( question.getQuestionText(), answers, answers.indexOf(question.getCorrect()) );
                    questions.add(testQuestion);
                }
                // -- create a list of strings from the picked subjects
                categoriesNames.add(category.getCategoryName());
            }

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm:ss");

            TestData testData = new TestData(Globals.getUserEmail(), subject.getSubjectName(), now.format(dateTimeFormatter), testVariant, questions, categoriesNames);
            ObjectMapper mapper =  Globals.getMapper();

            try {
                String fileNameString = "test_variant(" + testVariant + ")_" + Globals.getUserName() + "_" + Globals.getUserSurname() + "_" + dateTimeFormatter.format(now);
                FileHandler.createFile(new String[]{ FileHandler.getTestJson(fileNameString) });

                File targetFile = new File(FileHandler.getTestJson(fileNameString));

                // FIXED: Serialize the base object container directly instead of passing string tokens
                mapper.writeValue(targetFile, testData);
                tests.put(testVariant, mapper.writeValueAsString(testData));

                PDF.createPDF(fileNameString, mapper.writeValueAsString(testData));

                System.out.println("[SYSTEM] Successfully generated variant payload to disk: " + targetFile.getName());

                Thread.sleep(500);
            } catch (Exception e) {
                System.err.println("Failed to serialize output sheet variant.");
                e.printStackTrace();
            }
        }
    }
}