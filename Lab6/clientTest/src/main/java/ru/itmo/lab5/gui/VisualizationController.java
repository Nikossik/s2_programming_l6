package ru.itmo.lab5.gui;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Popup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import ru.itmo.lab5.data.models.*;
import ru.itmo.lab5.network.Client;
import ru.itmo.lab5.util.Task;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.*;

public class VisualizationController {

    @FXML
    private ImageView mapView;
    @FXML
    private Button backToTable;
    private final ResourceBundle bundle;
    private final Client client;
    private final Locale locale;
    private final String username;
    private Timer timer;
    private final HashMap<Integer, Circle> circleMap = new HashMap<>();

    VisualizationController(Client client, Locale locale, String username) {
        this.locale = locale;
        this.client = client;
        this.bundle = ResourceBundle.getBundle("visualization", locale);
        this.username = username;
    }

    public void initialize() {
        LinkedList<DisplayTicket> tickets = null;
        try {
            tickets = getTicketsFromServer();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        Image image = new Image(Objects.requireNonNull(getClass().getClassLoader().getResource("map.png")).toExternalForm());

        setImage(image);

        if (tickets != null) {
            for (DisplayTicket ticket : tickets) {
                drawCircles(ticket);
            }
        }

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateMap();
            }
        }, 0, 5000);
    }

    private void updateMap() {
        LinkedList<DisplayTicket> tickets = null;
        try {
            tickets = getTicketsFromServer();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (tickets != null) {
            LinkedList<DisplayTicket> finalTickets = tickets;
            Platform.runLater(() -> {
                Set<Integer> currentTicketIds = new HashSet<>();
                for (DisplayTicket ticket : finalTickets) {
                    currentTicketIds.add(ticket.getId());
                }

                circleMap.entrySet().removeIf(entry -> {
                    if (!currentTicketIds.contains(entry.getKey())) {
                        ((Pane) mapView.getParent()).getChildren().remove(entry.getValue());
                        return true;
                    }
                    return false;
                });

                for (DisplayTicket ticket : finalTickets) {
                    Circle existingCircle = circleMap.get(ticket.getId());
                    if (existingCircle != null) {
                        updateCircle(existingCircle, ticket);
                    } else {
                        drawCircles(ticket);
                    }
                }
            });
        }
    }

    private void editTicket(DisplayTicket ticket) {
        if (!ticket.getUsername().equals(username)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(bundle.getString("errorTitle"));
            alert.setHeaderText(null);
            alert.setContentText(bundle.getString("errorNotCreator"));
            alert.showAndWait();
            return;
        }

        Dialog<Ticket> dialog = new Dialog<>();
        dialog.setTitle(bundle.getString("updateButtonText"));
        dialog.setHeaderText(null);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField(ticket.getName());
        TextField xCoordField = new TextField(String.valueOf(ticket.getCoordinates().getX()));
        TextField yCoordField = new TextField(String.valueOf(ticket.getCoordinates().getY()));
        TextField priceField = new TextField(String.valueOf(ticket.getPrice()));
        ChoiceBox<TicketType> typeBox = new ChoiceBox<>(FXCollections.observableArrayList(TicketType.values()));
        typeBox.setValue(ticket.getType());
        TextField venueNameField = new TextField(ticket.getVenue().getName());
        TextField capacityField = new TextField(String.valueOf(ticket.getVenue().getCapacity()));
        ChoiceBox<VenueType> venueTypeBox = new ChoiceBox<>(FXCollections.observableArrayList(VenueType.values()));
        venueTypeBox.setValue(ticket.getVenue().getType());
        TextField addressStreetField = new TextField(ticket.getVenue().getAddress().getStreet());
        TextField zipCodeField = new TextField(ticket.getVenue().getAddress().getZipCode());
        TextField xLocField = new TextField(String.valueOf(ticket.getVenue().getAddress().getTown().getX()));
        TextField yLocField = new TextField(String.valueOf(ticket.getVenue().getAddress().getTown().getY()));
        TextField nameLocField = new TextField(ticket.getVenue().getAddress().getTown().getName());

        grid.add(new Label(bundle.getString("name") + ":"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label(bundle.getString("coordinates") + ":"), 0, 1);
        grid.add(xCoordField, 1, 1);
        grid.add(yCoordField, 2, 1);
        grid.add(new Label(bundle.getString("price") + ":"), 0, 2);
        grid.add(priceField, 1, 2);
        grid.add(new Label(bundle.getString("type") + ":"), 0, 3);
        grid.add(typeBox, 1, 3);
        grid.add(new Label(bundle.getString("venueName") + ":"), 0, 4);
        grid.add(venueNameField, 1, 4);
        grid.add(new Label(bundle.getString("capacity") + ":"), 0, 5);
        grid.add(capacityField, 1, 5);
        grid.add(new Label(bundle.getString("venueType") + ":"), 0, 6);
        grid.add(venueTypeBox, 1, 6);
        grid.add(new Label(bundle.getString("addressStreet") + ":"), 0, 7);
        grid.add(addressStreetField, 1, 7);
        grid.add(new Label(bundle.getString("zipCode") + ":"), 0, 8);
        grid.add(zipCodeField, 1, 8);
        grid.add(new Label(bundle.getString("location") + ":"), 0, 9);
        grid.add(xLocField, 1, 9);
        grid.add(yLocField, 2, 9);
        grid.add(nameLocField, 4, 9);

        ButtonType updateButton = new ButtonType(bundle.getString("updateButtonText"), ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType(bundle.getString("cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButton, cancelButton);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButton) {
                try {
                    String name = nameField.getText();
                    int xCoord = Integer.parseInt(xCoordField.getText());
                    double yCoord = Double.parseDouble(yCoordField.getText());
                    Long price = Long.parseLong(priceField.getText());
                    TicketType type = typeBox.getValue();
                    String venueName = venueNameField.getText();
                    int capacity = Integer.parseInt(capacityField.getText());
                    VenueType venueType = venueTypeBox.getValue();
                    String street = addressStreetField.getText();
                    String zipCode = zipCodeField.getText();
                    int xLoc = Integer.parseInt(xLocField.getText());
                    double yLoc = Double.parseDouble(yLocField.getText());
                    String locName = nameLocField.getText();

                    Coordinates coordinates = new Coordinates(xCoord, yCoord);
                    Location location = new Location(xLoc, yLoc, locName);
                    Address address = new Address(street, zipCode, location);
                    Venue venue = new Venue(venueName, capacity, venueType, address);
                    return new Ticket(name, coordinates, price, type, venue, username);
                } catch (NumberFormatException | DateTimeParseException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle(bundle.getString("errorTitle"));
                    alert.setHeaderText(null);
                    alert.setContentText(bundle.getString("errorInvalidInput"));
                    alert.showAndWait();
                    return null;
                }
            } else {
                return null;
            }
        });

        Optional<Ticket> result = dialog.showAndWait();
        result.ifPresent(updatedTicket -> {
            try {
                Task updateTask = new Task(new String[]{"update_id", String.valueOf(ticket.getId())}, updatedTicket, username);
                Task responseTask = client.sendTask(updateTask);
                if ("true".equals(responseTask.getDescribe()[0])) {
                    updateCircle(circleMap.get(ticket.getId()), new DisplayTicket(updatedTicket));
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle(bundle.getString("errorTitle"));
                    alert.setHeaderText(null);
                    alert.setContentText(bundle.getString("errorUpdateFailed"));
                    alert.showAndWait();
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }


    private void updateCircle(Circle circle, DisplayTicket ticket) {
        double x = ticket.getVenue().getAddress().getTown().getX();
        double y = ticket.getVenue().getAddress().getTown().getY();

        double viewX = mapView.getLayoutX();
        double viewY = mapView.getLayoutY();

        double circleX = x - viewX;
        double circleY = y - viewY;

        circle.setCenterX(circleX);
        circle.setCenterY(circleY);
        circle.setRadius(10 + (ticket.getVenue().getCapacity() / 10.0));
        circle.setFill(getColorByUsername(ticket.getUsername()));
        circle.setId(Integer.toString(ticket.getId()));
    }

    private Color getColorByUsername(String username) {
        int hash = username.hashCode();
        int r = (hash & 0xFF0000) >> 16;
        int g = (hash & 0x00FF00) >> 8;
        int b = hash & 0x0000FF;
        return Color.rgb(r, g, b);
    }

    public void drawCircles(DisplayTicket ticket) {
        double x = ticket.getVenue().getAddress().getTown().getX();
        double y = ticket.getVenue().getAddress().getTown().getY();

        double viewX = mapView.getLayoutX();
        double viewY = mapView.getLayoutY();

        double circleX = x - viewX;
        double circleY = y - viewY;

        Color color = getColorByUsername(ticket.getUsername());
        double radius = 10 + (ticket.getVenue().getCapacity() / 10.0);

        Circle circle = new Circle(circleX, circleY, radius, color);
        Pane parent = (Pane) mapView.getParent();
        circle.setTranslateZ(100);

        int ticketId = ticket.getId();
        circle.setId(Integer.toString(ticketId));
        circleMap.put(ticketId, circle);

        circle.setOnMouseClicked(event -> {
            String circleId = ((Circle) event.getSource()).getId();
            DisplayTicket clickedTicket = getTicketById(Integer.parseInt(circleId));

            Popup popup = new Popup();
            BorderPane borderPane = new BorderPane();
            borderPane.setStyle("-fx-background-color: white; -fx-padding: 10px;");
            borderPane.setBorder(new Border(new BorderStroke(Color.BLACK,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

            Label header = new Label(clickedTicket.getName());
            header.setStyle("-fx-font-weight: bold; -fx-padding: 5px;");
            borderPane.setTop(header);

            Label content = new Label("ID: " + clickedTicket.getId() + "\n" + bundle.getString("creator") + clickedTicket.getUsername());
            content.setStyle("-fx-padding: 5px;");
            borderPane.setCenter(content);

            Button editButton = new Button(bundle.getString("editButtonText"));
            editButton.setOnAction(e -> {
                popup.hide();
                editTicket(clickedTicket);
            });

            VBox vbox = new VBox(content, editButton);
            borderPane.setCenter(vbox);

            popup.getContent().add(borderPane);

            popup.setX(event.getScreenX());
            popup.setY(event.getScreenY());
            popup.setAutoHide(true);
            popup.show(parent.getScene().getWindow());
        });

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1.5), circle);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();

        parent.getChildren().add(circle);
    }


    public void setImage(Image image) {
        mapView.setImage(image);
    }

    public DisplayTicket getTicketById(int id) {
        try {
            LinkedList<DisplayTicket> tickets = getTicketsFromServer();
            for (DisplayTicket ticket : tickets) {
                if (ticket.getId() == id) {
                    return ticket;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private LinkedList<DisplayTicket> getTicketsFromServer() throws IOException, ClassNotFoundException {
        Task requestTask = new Task(new String[]{"show"});
        Task responseTask = client.sendTask(requestTask);
        LinkedList<DisplayTicket> tickets = new LinkedList<>();
        if (responseTask != null && responseTask.getTickets() != null) {
            for (Ticket ticket : responseTask.getTickets()) {
                tickets.add(new DisplayTicket(ticket));
            }
        }
        return tickets;
    }

    @FXML
    public void handleBackToTable(ActionEvent event) {
        timer.cancel();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Table.fxml"));
        ResourceBundle bundle = ResourceBundle.getBundle("table", locale);
        loader.setController(new TableController(client, locale, username));
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
}
