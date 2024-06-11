package ru.itmo.lab5.gui;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import ru.itmo.lab5.network.Client;
import ru.itmo.lab5.util.Task;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AuthController {

    Locale locale;

    @FXML private Button loginButton;
    @FXML private Button registerButton;
    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private StackPane popupPane;
    @FXML private StackPane popupPaneRegister;
    private final Client client;

    public AuthController(Client client, Locale locale) {
        this.locale = locale;
        this.client = client;
    }

    @FXML
    public void showErrorPopup() {
        popupPane.setVisible(true);
        PauseTransition visiblePause = new PauseTransition(Duration.seconds(3));
        visiblePause.setOnFinished(event -> popupPane.setVisible(false));
        visiblePause.play();
    }

    @FXML
    public void showErrorPopupRegister() {
        popupPaneRegister.setVisible(true);
        PauseTransition visiblePause = new PauseTransition(Duration.seconds(3));
        visiblePause.setOnFinished(event -> popupPaneRegister.setVisible(false));
        visiblePause.play();
    }

    @FXML
    public void showTable(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Table.fxml"));
        ResourceBundle bundle = ResourceBundle.getBundle("table", locale);
        loader.setController(new TableController(client, locale, loginField.getText()));
        loader.setResources(bundle);
        Parent root;
        try {
            root = loader.load();
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene nextScene = new Scene(root);
            primaryStage.setScene(nextScene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleButton(ActionEvent event) {
        String login = loginField.getText();
        String password = passwordField.getText();
        Task task = new Task();
        task.setUsername(login);
        task.setPassword(password);

        if (event.getSource() == loginButton) {
            task.setDescribe(new String[]{"login"});
            try {
                System.out.println("Sending login task to server...");
                Task responseTask = client.sendTask(task);
                System.out.println("Received response: " + responseTask.getDescribe()[0]);
                boolean loginSuccess = Boolean.parseBoolean(responseTask.getDescribe()[0]);
                if (loginSuccess) {
                    showTable(event);
                } else {
                    showErrorPopup();
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                showErrorPopup();
            }
        }
        if (event.getSource() == registerButton) {
            task.setDescribe(new String[]{"register"});
            try {
                System.out.println("Sending register task to server...");
                Task responseTask = client.sendTask(task);
                System.out.println("Received response: " + responseTask.getDescribe()[0]);
                boolean registerSuccess = Boolean.parseBoolean(responseTask.getDescribe()[0]);
                if (registerSuccess) {
                    System.out.println("Done");
                    //showTable(event);
                } else {
                    showErrorPopupRegister();
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                showErrorPopupRegister();
            }
        }
    }
}
