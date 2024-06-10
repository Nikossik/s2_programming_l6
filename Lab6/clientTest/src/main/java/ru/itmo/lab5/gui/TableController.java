//package ru.itmo.lab5.gui;
//
//import java.io.IOException;
//import java.time.LocalDate;
//import java.time.format.DateTimeParseException;
//import java.util.LinkedList;
//import java.util.Locale;
//import java.util.Optional;
//import java.util.ResourceBundle;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import ru.itmo.lab5.data.models.*;
//import ru.itmo.lab5.network.Client;
//import ru.itmo.lab5.util.Task;
//
//import javafx.collections.FXCollections;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.Node;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.layout.GridPane;
//import javafx.scene.layout.HBox;
//import javafx.stage.Stage;
//
//public class TableController {
//
//    private final Client client;
//    private final Locale locale;
//    private final ResourceBundle bundle;
//
//    @FXML
//    private Button addButton;
//    @FXML
//    private Button removeButton;
//    @FXML
//    private Button removeGreaterButton;
//    @FXML
//    private TableView<DisplayTicket> tableView;
//    @FXML
//    private TableColumn<DisplayTicket, String> idColumn;
//    @FXML
//    private TableColumn<DisplayTicket, String> nameColumn;
//    @FXML
//    private TableColumn<DisplayTicket, String> latitudeColumn;
//    @FXML
//    private TableColumn<DisplayTicket, String> longitudeColumn;
//    @FXML
//    private TableColumn<DisplayTicket, LocalDate> creationDateColumn;
//    @FXML
//    private TableColumn<DisplayTicket, Long> priceColumn;
//    @FXML
//    private TableColumn<DisplayTicket, TicketType> typeColumn;
//    @FXML
//    private TableColumn<DisplayTicket, String> venueColumn;
//    @FXML
//    private TableColumn<DisplayTicket, String> creatorColumn;
//    @FXML
//    private HBox bottomHBox;
//
//    public TableController(Client client, Locale locale) {
//        this.client = client;
//        this.locale = locale;
//        this.bundle = ResourceBundle.getBundle("table", locale);
//    }
//
//    public void initialize() {
//        Label label = new Label(UserHelper.logged_user.getLogin());
//        label.setAlignment(Pos.CENTER);
//        label.setStyle("-fx-font-size: 24;");
//        bottomHBox.getChildren().add(label);
//
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                LinkedList<DisplayTicket> linkedList = null;
//                try {
//                    Task requestTask = new Task(new String[]{"loadCollection"});
//                    if (client.sendTask(requestTask)) {
//                        linkedList = client.loadCollection();
//                        var tickets = FXCollections.observableArrayList(linkedList);
//                        idColumn.setCellValueFactory(data -> data.getValue().getIdProperty().asString());
//                        nameColumn.setCellValueFactory(data -> data.getValue().getNameProperty());
//                        latitudeColumn.setCellValueFactory(data -> data.getValue().getCoordinatesProperty().getValue().getXProperty().asString());
//                        longitudeColumn.setCellValueFactory(data -> data.getValue().getCoordinatesProperty().getValue().getYProperty().asString());
//                        creationDateColumn.setCellValueFactory(data -> data.getValue().getCreationDateProperty());
//                        priceColumn.setCellValueFactory(data -> data.getValue().getPriceProperty().asObject());
//                        typeColumn.setCellValueFactory(data -> data.getValue().getTypeProperty());
//                        venueColumn.setCellValueFactory(data -> data.getValue().getVenueProperty().asString());
//                        tableView.setItems(tickets);
//                    }
//                } catch (IOException | ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, 0, 5000);
//    }
//
//    @FXML
//    private void handleLogoutButton(ActionEvent event) throws IOException {
//        ResourceBundle bundle = ResourceBundle.getBundle("auth", locale);
//        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Auth.fxml"));
//        loader.setController(new AuthController(client, locale));
//        loader.setResources(bundle);
//        Parent root;
//        try {
//            root = loader.load();
//            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            Scene nextScene = new Scene(root);
//            primaryStage.setScene(nextScene);
//            primaryStage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @FXML
//    private void handleUpdate(ActionEvent event) {
//        TextInputDialog toRemove = new TextInputDialog();
//        toRemove.setHeaderText(null);
//        toRemove.setContentText(bundle.getString("idPrompt"));
//        Optional<String> res = toRemove.showAndWait();
//
//        if (!res.isPresent() || res.get().isEmpty()) {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Error");
//            alert.setHeaderText(null);
//            alert.setContentText(bundle.getString("idPrompt"));
//            alert.showAndWait();
//            return;
//        }
//        res.ifPresent(id -> {
//            Dialog<Ticket> dialog = new Dialog<>();
//            dialog.setTitle(bundle.getString("updateButtonText"));
//            dialog.setHeaderText(null);
//
//            GridPane grid = new GridPane();
//            grid.setHgap(10);
//            grid.setVgap(10);
//            grid.setPadding(new Insets(20, 150, 10, 10));
//
//            TextField nameField = new TextField();
//            nameField.setPromptText(bundle.getString("name"));
//            TextField xCoordField = new TextField();
//            xCoordField.setPromptText(bundle.getString("coordinatesX"));
//            TextField yCoordField = new TextField();
//            yCoordField.setPromptText(bundle.getString("coordinatesY"));
//            TextField priceField = new TextField();
//            priceField.setPromptText(bundle.getString("price"));
//            ChoiceBox<TicketType> typeBox = new ChoiceBox<>(FXCollections.observableArrayList(TicketType.values()));
//            typeBox.getSelectionModel().selectFirst();
//            TextField venueNameField = new TextField();
//            venueNameField.setPromptText(bundle.getString("venueName"));
//            TextField capacityField = new TextField();
//            capacityField.setPromptText(bundle.getString("capacity"));
//            ChoiceBox<VenueType> venueTypeBox = new ChoiceBox<>(FXCollections.observableArrayList(VenueType.values()));
//            venueTypeBox.getSelectionModel().selectFirst();
//            TextField addressStreetField = new TextField();
//            addressStreetField.setPromptText(bundle.getString("addressStreet"));
//            TextField zipCodeField = new TextField();
//            zipCodeField.setPromptText(bundle.getString("zipCode"));
//            TextField xLocField = new TextField();
//            xLocField.setPromptText("X " + bundle.getString("location"));
//            TextField yLocField = new TextField();
//            yLocField.setPromptText("Y " + bundle.getString("location"));
//            TextField zLocField = new TextField();
//            zLocField.setPromptText("Z " + bundle.getString("location"));
//            TextField nameLocField = new TextField();
//            nameLocField.setPromptText(bundle.getString("location"));
//
//            grid.add(new Label(bundle.getString("name") + ":"), 0, 0);
//            grid.add(nameField, 1, 0);
//            grid.add(new Label(bundle.getString("coordinates") + ":"), 0, 1);
//            grid.add(xCoordField, 1, 1);
//            grid.add(yCoordField, 2, 1);
//            grid.add(new Label(bundle.getString("price") + ":"), 0, 2);
//            grid.add(priceField, 1, 2);
//            grid.add(new Label(bundle.getString("type") + ":"), 0, 3);
//            grid.add(typeBox, 1, 3);
//            grid.add(new Label(bundle.getString("venueName") + ":"), 0, 4);
//            grid.add(venueNameField, 1, 4);
//            grid.add(new Label(bundle.getString("capacity") + ":"), 0, 5);
//            grid.add(capacityField, 1, 5);
//            grid.add(new Label(bundle.getString("venueType") + ":"), 0, 6);
//            grid.add(venueTypeBox, 1, 6);
//            grid.add(new Label(bundle.getString("addressStreet") + ":"), 0, 7);
//            grid.add(addressStreetField, 1, 7);
//            grid.add(new Label(bundle.getString("zipCode") + ":"), 0, 8);
//            grid.add(zipCodeField, 1, 8);
//            grid.add(new Label(bundle.getString("location") + ":"), 0, 9);
//            grid.add(xLocField, 1, 9);
//            grid.add(yLocField, 2, 9);
//            grid.add(zLocField, 3, 9);
//            grid.add(nameLocField, 4, 9);
//
//            ButtonType updateButton = new ButtonType(bundle.getString("updateButtonText"), ButtonBar.ButtonData.OK_DONE);
//            ButtonType cancelButton = new ButtonType(bundle.getString("cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);
//            dialog.getDialogPane().getButtonTypes().addAll(updateButton, cancelButton);
//
//            dialog.getDialogPane().setContent(grid);
//
//            dialog.setResultConverter(dialogButton -> {
//                if (dialogButton == updateButton) {
//                    try {
//                        String name = nameField.getText();
//                        int xCoord = Integer.parseInt(xCoordField.getText());
//                        double yCoord = Double.parseDouble(yCoordField.getText());
//                        Long price = Long.parseLong(priceField.getText());
//                        TicketType type = typeBox.getValue();
//                        String venueName = venueNameField.getText();
//                        int capacity = Integer.parseInt(capacityField.getText());
//                        VenueType venueType = venueTypeBox.getValue();
//                        String street = addressStreetField.getText();
//                        String zipCode = zipCodeField.getText();
//                        int xLoc = Integer.parseInt(xLocField.getText());
//                        double yLoc = Double.parseDouble(yLocField.getText());
//                        double zLoc = Double.parseDouble(zLocField.getText());
//                        String locName = nameLocField.getText();
//
//                        Coordinates coordinates = new Coordinates(xCoord, yCoord);
//                        Location location = new Location(xLoc, yLoc, locName);
//                        Address address = new Address(street, zipCode, location);
//                        Venue venue = new Venue(venueName, capacity, venueType, address);
//                        return new Ticket(name, coordinates, price, type, venue, UserHelper.logged_user.getLogin());
//                    } catch (NumberFormatException | DateTimeParseException e) {
//                        Alert alert = new Alert(Alert.AlertType.ERROR);
//                        alert.setTitle("Error");
//                        alert.setHeaderText(null);
//                        alert.setContentText("Invalid input values. Please enter valid input values.");
//                        alert.showAndWait();
//                        return null;
//                    }
//                } else {
//                    return null;
//                }
//            });
//
//            Optional<Ticket> result = dialog.showAndWait();
//            result.ifPresent(ticket -> {
//                try {
//                    client.sendTask(new Task(new String[]{"update", id}, ticket, UserHelper.logged_user.getLogin(), ""));
//                } catch (IOException | ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
//            });
//        });
//    }
//
//    @FXML
//    private void handleAddButton() {
//        Dialog<Ticket> dialog = new Dialog<>();
//        dialog.setTitle(bundle.getString("addButtonText"));
//        dialog.setHeaderText(null);
//
//        GridPane grid = new GridPane();
//        grid.setHgap(10);
//        grid.setVgap(10);
//        grid.setPadding(new Insets(20, 150, 10, 10));
//
//        TextField nameField = new TextField();
//        nameField.setPromptText(bundle.getString("name"));
//        TextField xCoordField = new TextField();
//        xCoordField.setPromptText(bundle.getString("coordinatesX"));
//        TextField yCoordField = new TextField();
//        yCoordField.setPromptText(bundle.getString("coordinatesY"));
//        TextField priceField = new TextField();
//        priceField.setPromptText(bundle.getString("price"));
//        ChoiceBox<TicketType> typeBox = new ChoiceBox<>(FXCollections.observableArrayList(TicketType.values()));
//        typeBox.getSelectionModel().selectFirst();
//        TextField venueNameField = new TextField();
//        venueNameField.setPromptText(bundle.getString("venueName"));
//        TextField capacityField = new TextField();
//        capacityField.setPromptText(bundle.getString("capacity"));
//        ChoiceBox<VenueType> venueTypeBox = new ChoiceBox<>(FXCollections.observableArrayList(VenueType.values()));
//        venueTypeBox.getSelectionModel().selectFirst();
//        TextField addressStreetField = new TextField();
//        addressStreetField.setPromptText(bundle.getString("addressStreet"));
//        TextField zipCodeField = new TextField();
//        zipCodeField.setPromptText(bundle.getString("zipCode"));
//        TextField xLocField = new TextField();
//        xLocField.setPromptText("X " + bundle.getString("location"));
//        TextField yLocField = new TextField();
//        yLocField.setPromptText("Y " + bundle.getString("location"));
//        TextField zLocField = new TextField();
//        zLocField.setPromptText("Z " + bundle.getString("location"));
//        TextField nameLocField = new TextField();
//        nameLocField.setPromptText(bundle.getString("location"));
//
//        grid.add(new Label(bundle.getString("name") + ":"), 0, 0);
//        grid.add(nameField, 1, 0);
//        grid.add(new Label(bundle.getString("coordinates") + ":"), 0, 1);
//        grid.add(xCoordField, 1, 1);
//        grid.add(yCoordField, 2, 1);
//        grid.add(new Label(bundle.getString("price") + ":"), 0, 2);
//        grid.add(priceField, 1, 2);
//        grid.add(new Label(bundle.getString("type") + ":"), 0, 3);
//        grid.add(typeBox, 1, 3);
//        grid.add(new Label(bundle.getString("venueName") + ":"), 0, 4);
//        grid.add(venueNameField, 1, 4);
//        grid.add(new Label(bundle.getString("capacity") + ":"), 0, 5);
//        grid.add(capacityField, 1, 5);
//        grid.add(new Label(bundle.getString("venueType") + ":"), 0, 6);
//        grid.add(venueTypeBox, 1, 6);
//        grid.add(new Label(bundle.getString("addressStreet") + ":"), 0, 7);
//        grid.add(addressStreetField, 1, 7);
//        grid.add(new Label(bundle.getString("zipCode") + ":"), 0, 8);
//        grid.add(zipCodeField, 1, 8);
//        grid.add(new Label(bundle.getString("location") + ":"), 0, 9);
//        grid.add(xLocField, 1, 9);
//        grid.add(yLocField, 2, 9);
//        grid.add(zLocField, 3, 9);
//        grid.add(nameLocField, 4, 9);
//
//        ButtonType addButton = new ButtonType(bundle.getString("addButtonText"), ButtonBar.ButtonData.OK_DONE);
//        ButtonType cancelButton = new ButtonType(bundle.getString("cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);
//        dialog.getDialogPane().getButtonTypes().addAll(addButton, cancelButton);
//
//        dialog.getDialogPane().setContent(grid);
//
//        dialog.setResultConverter(dialogButton -> {
//            if (dialogButton == addButton) {
//                try {
//                    String name = nameField.getText();
//                    int xCoord = Integer.parseInt(xCoordField.getText());
//                    double yCoord = Double.parseDouble(yCoordField.getText());
//                    Long price = Long.parseLong(priceField.getText());
//                    TicketType type = typeBox.getValue();
//                    String venueName = venueNameField.getText();
//                    int capacity = Integer.parseInt(capacityField.getText());
//                    VenueType venueType = venueTypeBox.getValue();
//                    String street = addressStreetField.getText();
//                    String zipCode = zipCodeField.getText();
//                    int xLoc = Integer.parseInt(xLocField.getText());
//                    double yLoc = Double.parseDouble(yLocField.getText());
//                    double zLoc = Double.parseDouble(zLocField.getText());
//                    String locName = nameLocField.getText();
//
//                    Coordinates coordinates = new Coordinates(xCoord, yCoord);
//                    Location location = new Location(xLoc, yLoc, locName);
//                    Address address = new Address(street, zipCode, location);
//                    Venue venue = new Venue(venueName, capacity, venueType, address);
//                    return new Ticket(name, coordinates, price, type, venue, UserHelper.logged_user.getLogin());
//                } catch (NumberFormatException | DateTimeParseException e) {
//                    Alert alert = new Alert(Alert.AlertType.ERROR);
//                    alert.setTitle("Error");
//                    alert.setHeaderText(null);
//                    alert.setContentText("Invalid input values. Please enter valid input values.");
//                    alert.showAndWait();
//                    return null;
//                }
//            } else {
//                return null;
//            }
//        });
//
//        Optional<Ticket> result = dialog.showAndWait();
//        result.ifPresent(ticket -> {
//            try {
//                client.sendTask(new Task(new String[]{"add"}, ticket, UserHelper.logged_user.getLogin(), ""));
//            } catch (IOException | ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//        });
//    }
//
//    @FXML
//    private void handleRemoveById() {
//        TextInputDialog toRemove = new TextInputDialog();
//        toRemove.setHeaderText(null);
//        toRemove.setContentText(bundle.getString("idPrompt"));
//        Optional<String> res = toRemove.showAndWait();
//
//        if (!res.isPresent() || res.get().isEmpty()) {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Error");
//            alert.setHeaderText(null);
//            alert.setContentText(bundle.getString("idPrompt"));
//            alert.showAndWait();
//            return;
//        }
//
//        try {
//            client.sendTask(new Task(new String[]{"remove_by_id", res.get()}));
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @FXML
//    private void handleRemoveGreater() {
//        TextInputDialog toRemove = new TextInputDialog();
//        toRemove.setHeaderText(null);
//        toRemove.setContentText(bundle.getString("idPrompt"));
//        Optional<String> res = toRemove.showAndWait();
//
//        if (!res.isPresent() || res.get().isEmpty()) {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Error");
//            alert.setHeaderText(null);
//            alert.setContentText(bundle.getString("idPrompt"));
//            alert.showAndWait();
//            return;
//        }
//
//        try {
//            client.sendTask(new Task(new String[]{"remove_greater", res.get()}));
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @FXML
//    private void handleVisualizationButton(ActionEvent event) {
//        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Map.fxml"));
//        ResourceBundle newBundle = ResourceBundle.getBundle("visualization", locale);
//        loader.setController(new VisualizationController(client, locale));
//        loader.setResources(newBundle);
//        Parent root;
//        try {
//            root = loader.load();
//            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            Scene nextScene = new Scene(root);
//            primaryStage.setScene(nextScene);
//            primaryStage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @FXML
//    private void handleShuffle() {
//        try {
//            client.sendTask(new Task(new String[]{"shuffle"}));
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @FXML
//    private void handleReorder() {
//        try {
//            client.sendTask(new Task(new String[]{"reorder"}));
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//}
