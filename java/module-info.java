module com.testgen.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.annotation;
    requires tools.jackson.databind;
    requires java.sql;


    opens com.testgen.demo to javafx.fxml;
    exports com.testgen.demo;
}