module com.example.psm {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.xml;

    opens com.example.psm to javafx.fxml;
    exports com.example.psm;
}