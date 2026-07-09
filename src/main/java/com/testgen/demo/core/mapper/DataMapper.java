package com.testgen.demo.core.mapper;

import com.testgen.demo.core.model.TestData;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataMapper {

    public Context setData(List<TestData> testDataList) {

        Context context = new Context();
        Map<String, Object> data = new HashMap<>();

        data.put("test", testDataList);
        context.setVariables(data);

        return context;
    }
}
