module com.example.gestiondecommades {    // Updated module name
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.gestiondecommades to javafx.fxml;
    exports com.example.gestiondecommades;
}