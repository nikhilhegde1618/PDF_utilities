package application;


import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Alert {
    Alert(String heading, String msg) {
        Stage stageAlert = new Stage();
        stageAlert.setTitle("Alert");
        VBox alertroot;
        try {
            alertroot = (VBox)FXMLLoader.load(getClass().getResource("alert.fxml"));
            Label done = (Label)alertroot.lookup("#done");
            Label alertMsg = (Label)alertroot.lookup("#successMsg");
            done.setText(heading);
            alertMsg.setText(msg);
            alertMsg.setWrapText(true);
            stageAlert.setScene(new Scene(alertroot, 400, 220));
            stageAlert.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
