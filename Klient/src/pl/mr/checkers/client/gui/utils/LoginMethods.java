package pl.mr.checkers.client.gui.utils;

import javafx.scene.control.Label;
import javafx.util.Pair;
import pl.mr.checkers.client.gui.AbstractController;
import pl.mr.checkers.model.PackageType;

public class LoginMethods extends Methods {

    //sprawdzanie czy login się nie powtarza
    public boolean isWrongLogin(AbstractController controller, Label errorMessage) {

        Pair<String, Object> result = sendPackage(controller, PackageType.LOGIN, null);

        if (result.getKey().equals("OK")) {
            return false;
        } else if (result.getKey().equals("RIP")) {
            errorMessage.setText("Server error connection");
            return true;
        } else {
//            errorMessage.setText(result);
            errorMessage.setText("This name already exists");
            return true;
        }
    }
}
