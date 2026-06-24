module com.testgen.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.testgen.demo to javafx.fxml;
    exports com.testgen.demo;
}