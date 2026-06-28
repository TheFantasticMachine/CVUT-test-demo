module com.testgen.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.annotation;
    requires tools.jackson.databind;
    requires java.sql;
    requires annotations;
    requires org.apache.commons.lang3;


    opens com.testgen.demo to javafx.fxml;
    exports com.testgen.demo;
    opens com.testgen.demo.core.config to tools.jackson.databind;
}