module com.aleksa.jpasslocker {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens com.aleksa.jpasslocker to javafx.fxml;
    exports com.aleksa.jpasslocker;
}