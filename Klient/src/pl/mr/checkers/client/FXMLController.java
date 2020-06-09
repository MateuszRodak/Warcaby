package pl.mr.checkers.client;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class FXMLController {
    
    @FXML
    private Label label;
    @FXML
    private Label nazwagracza;

    public void initialize() {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        label.setText("Hello, JavaFX " + javafxVersion + "\nRunning on Java " + javaVersion + ".");
    }    
}
