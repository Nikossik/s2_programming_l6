package ru.itmo.lab5.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import ru.itmo.lab5.gui.AuthController;
import ru.itmo.lab5.network.Client;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class App extends Application {

    private Locale currentLocale = Locale.forLanguageTag("ru_RU");
    private ResourceBundle bundle = ResourceBundle.getBundle("auth", currentLocale);
    private Client client;

    @Override
    public void start(Stage primaryStage) throws Exception {
        client = new Client();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("Auth.fxml"), bundle);
        AuthController authController = new AuthController(client, currentLocale);
        fxmlLoader.setController(authController);

        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        HBox buttonBox = getButtonBox(primaryStage, scene);

        if (root instanceof Pane) {
            ((Pane) root).getChildren().addAll(buttonBox);
        }

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void changeLocale(Locale newLocale, Stage stage, Scene scene) {
        if (!newLocale.equals(currentLocale)) {
            currentLocale = newLocale;
            bundle = ResourceBundle.getBundle("auth", currentLocale);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("Auth.fxml"), bundle);
            AuthController authController = new AuthController(client, currentLocale);
            fxmlLoader.setController(authController);

            try {
                Parent root = fxmlLoader.load();

                HBox buttonBox = getButtonBox(stage, scene);

                if (root instanceof Pane) {
                    ((Pane) root).getChildren().addAll(buttonBox);
                }

                scene.setRoot(root);

                stage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private HBox getButtonBox(Stage stage, Scene scene) {
        Button englishButton = new Button("English");
        englishButton.setOnAction(event -> changeLocale(Locale.forLanguageTag("en_UK"), stage, scene));

        Button russianButton = new Button("Русский");
        russianButton.setOnAction(event -> changeLocale(new Locale("ru_RU"), stage, scene));

        Button daButton = new Button("Suomalainen");
        daButton.setOnAction(event -> changeLocale(new Locale("fi_FI"), stage, scene));

        Button nlButton = new Button("Shqiptare");
        nlButton.setOnAction(event -> changeLocale(new Locale("sq_AL"), stage, scene));

        HBox buttonBox = new HBox(10, englishButton, russianButton, daButton, nlButton);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        return buttonBox;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
