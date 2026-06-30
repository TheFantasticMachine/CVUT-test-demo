package com.testgen.demo.core.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.testgen.demo.Globals;
import com.testgen.demo.core.config.FileHandler;
import com.testgen.demo.core.engine.DatabaseLoader;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Category {

    private String categoryName;
    private int categoryID;
    @JsonAlias({"questions", "myQuestions", "questionSet"})
    private ArrayList<Question> questions;
    @JsonIgnore
    private Subject parentSubject;

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }
    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    @JsonIgnore
    public void setParentSubject(Subject parentSubject) {
        this.parentSubject = parentSubject;
    }

    public String getCategoryName() {
        return categoryName;
    }
    public int getCategoryID() {
        return categoryID;
    }
    public ArrayList<Question> getQuestions() {
        return questions;
    }
    @JsonIgnore
    public Subject getParentSubject() {
        return parentSubject;
    }

    @JsonIgnore
    public List<Question> getUniqueQuestions(int amountNeeded) {
        List<Question> selectionOutput = new ArrayList<>();

        if (questions == null || questions.isEmpty()) {
            return selectionOutput;
        }

        // Step 1: Isolate historically unused questions
        List<Question> unusedPool = new ArrayList<>();
        for (Question q : questions) {
            if (!Globals.getUsedQuestionIDs().contains(q.getQuestionID())) {
                unusedPool.add(q);
            }
        }

        Collections.shuffle(unusedPool);

        if (unusedPool.size() >= amountNeeded) {
            // Case A: Plenty of historically unique questions left
            selectionOutput.addAll(unusedPool.subList(0, amountNeeded));
        } else {
            // Case B: Pool runs dry! Grab all remaining unique ones first
            selectionOutput.addAll(unusedPool);
            int remainingSlotsNeeded = amountNeeded - selectionOutput.size();

            System.out.println("[ENGINE] Pool exhausted for category '" + categoryName + "'. Recycling history safely.");

            // Clear historical flags for this category from global RAM
            for (Question q : questions) {
                Globals.getUsedQuestionIDs().remove(Integer.valueOf(q.getQuestionID()));
            }

            // Step 2: Build the recycling pool
            List<Question> recycledPool = new ArrayList<>();
            for (Question q : questions) {
                // FIXED: Explicitly filter out questions that have ALREADY been picked for this active test!
                if (!selectionOutput.contains(q)) {
                    recycledPool.add(q);
                }
            }

            // SAFEGUARD EDGE CASE: If a user asks for 6 questions out of a pool of 5,
            // recycledPool will be empty because all 5 are already in selectionOutput.
            // If that absolute bottleneck happens, allow duplicates to prevent application crashes.
            if (recycledPool.isEmpty()) {
                recycledPool.addAll(questions);
            }

            Collections.shuffle(recycledPool);

            // Fill the remaining empty slots on the test sheet
            for (int i = 0; i < remainingSlotsNeeded && i < recycledPool.size(); i++) {
                selectionOutput.add(recycledPool.get(i));
            }
        }

        // Step 3: Commit the final selection to memory tracking
        for (Question pickedQuestion : selectionOutput) {
            Globals.markQuestionAsUsed(pickedQuestion.getQuestionID());
        }

        return selectionOutput;
    }
}
