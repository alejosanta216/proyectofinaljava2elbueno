module com.example.proyectofinaljava2 {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.proyectofinaljava2 to javafx.fxml;
    opens com.example.proyectofinaljava2.controller to javafx.fxml;
    opens com.example.proyectofinaljava2.model to javafx.fxml;

    exports com.example.proyectofinaljava2;
    exports com.example.proyectofinaljava2.controller;
    exports com.example.proyectofinaljava2.model;
}