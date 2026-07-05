package com.testgen.demo.core.repository;

import com.testgen.demo.core.entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class QuestionRepository implements JpaRepository<QuestionEntity, Integer> {
}
