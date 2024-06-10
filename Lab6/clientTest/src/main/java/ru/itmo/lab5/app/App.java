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

    private Locale currentLocale = Locale.forLanguageTag("en-UK");
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

        Button englishButton = new Button("English");
        englishButton.setOnAction(event -> changeLocale(Locale.forLanguageTag("en-UK"), primaryStage, scene));

        Button russianButton = new Button("�������");
        russianButton.setOnAction(event -> changeLocale(new Locale("ru-RU"), primaryStage, scene));

        HBox buttonBox = new HBox(10, englishButton, russianButton);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setAlignment(Pos.CENTER_LEFT);

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

                Button englishButton = new Button("English");
                englishButton.setOnAction(event -> changeLocale(Locale.forLanguageTag("en-UK"), stage, scene));

                Button russianButton = new Button("�������");
                russianButton.setOnAction(event -> changeLocale(new Locale("ru-RU"), stage, scene));

                HBox buttonBox = new HBox(10, englishButton, russianButton);
                buttonBox.setPadding(new Insets(10));
                buttonBox.setAlignment(Pos.CENTER_LEFT);

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

    public static void main(String[] args) {
        launch(args);
    }
}
