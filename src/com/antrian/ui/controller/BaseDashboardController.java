package com.antrian.ui.controller;

import com.antrian.core.model.Antrian;
import com.antrian.ui.Main;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.stream.Collectors;

public abstract class BaseDashboardController {
    @FXML protected ListView<Antrian> antrianListView;
    @FXML protected Label welcomeLabel;
    @FXML protected TextField searchField;
    @FXML protected ComboBox<String> poliFilterComboBox;
    @FXML protected ComboBox<String> statusFilterComboBox;

    protected ObservableList<Antrian> fullAntrianList = FXCollections.observableArrayList();
    private Timeline autoRefreshTimeline;

    @FXML
    public void initialize() {
        welcomeLabel.setText("Selamat Datang, " + Main.loggedInUser.getNama() + "!");
        
        setupFilterControls();
        setupListViewFactory();
        loadAntrianData();
        startAutoRefresh();
    }
    
    private void startAutoRefresh() {
        autoRefreshTimeline = new Timeline(
            new KeyFrame(Duration.seconds(5), event -> {
                System.out.println("Refreshing dashboard data...");
                loadAntrianData();
            })
        );
        autoRefreshTimeline.setCycleCount(Timeline.INDEFINITE);
        autoRefreshTimeline.play();
    }

    private void setupFilterControls() {
        poliFilterComboBox.getItems().addAll("Semua Poli", "Poli Umum", "Poli Gigi", "Poli Anak", "Poli Jantung");
        poliFilterComboBox.setValue("Semua Poli");
        statusFilterComboBox.getItems().addAll("Semua Status", "Baru", "Sedang Berlangsung", "Selesai", "Batal");
        statusFilterComboBox.setValue("Semua Status");

        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        poliFilterComboBox.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        statusFilterComboBox.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
    }

    protected void applyFilters() {
        Antrian selected = antrianListView.getSelectionModel().getSelectedItem();

        var filteredList = fullAntrianList.stream()
            .filter(antrian -> {
                String searchText = searchField.getText().toLowerCase();
                if (searchText.isEmpty()) return true;
                return antrian.getNamaPasien().toLowerCase().contains(searchText);
            })
            .filter(antrian -> {
                String selectedPoli = poliFilterComboBox.getValue();
                if (selectedPoli == null || selectedPoli.equals("Semua Poli")) return true;
                return antrian.getPoli().equals(selectedPoli);
            })
            .filter(antrian -> {
                String selectedStatus = statusFilterComboBox.getValue();
                if (selectedStatus == null || selectedStatus.equals("Semua Status")) return true;
                return antrian.getStatus().equals(selectedStatus);
            })
            .collect(Collectors.toList());

        antrianListView.setItems(FXCollections.observableArrayList(filteredList));
        
        if (selected != null && antrianListView.getItems().contains(selected)) {
            antrianListView.getSelectionModel().select(selected);
        }
    }

    private void setupListViewFactory() {
        antrianListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Antrian item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Circle statusCircle = new Circle(8, getStatusColor(item.getStatus()));

                    Label antrianIdLabel = new Label("Antrian #" + item.getId());
                    antrianIdLabel.getStyleClass().add("card-title");
                    Label poliLabel = new Label(item.getPoli());
                    poliLabel.getStyleClass().add("card-meta-text");
                    VBox titleBox = new VBox(2, antrianIdLabel, poliLabel);

                    Label namaPasienLabel = new Label(item.getNamaPasien());
                    namaPasienLabel.getStyleClass().add("card-subtitle");

                    Region spacer = new Region();
                    VBox.setVgrow(spacer, Priority.ALWAYS);

                    Label postedAtLabel = new Label("Dibuat pada " + item.getFormattedTimestamp());
                    postedAtLabel.getStyleClass().add("card-meta-text");
                    
                    HBox metaBox = new HBox(10, postedAtLabel);
                    metaBox.setAlignment(Pos.CENTER_LEFT);

                    if ("admin".equalsIgnoreCase(Main.loggedInUser.getRole())) {
                        Button detailButton = new Button("Lihat Detail");
                        detailButton.setOnAction(event -> showDetailAntrian(item));
                        metaBox.getChildren().add(detailButton);
                    }

                    VBox contentBox = new VBox(10, titleBox, namaPasienLabel, spacer, metaBox);
                    
                    HBox cardContent = new HBox(20, statusCircle, contentBox);
                    HBox.setHgrow(contentBox, Priority.ALWAYS);
                    cardContent.setAlignment(Pos.CENTER_LEFT);
                    
                    BorderPane cardPane = new BorderPane(cardContent);
                    cardPane.getStyleClass().add("card-pane");
                    
                    cardPane.setMaxWidth(Double.MAX_VALUE);
                    
                    setGraphic(cardPane);
                }
            }
        });
    }
    
    protected void showDetailAntrian(Antrian antrian) {
        if(autoRefreshTimeline != null) autoRefreshTimeline.pause();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AntrianDetail.fxml"));
            Parent parent = loader.load();
            AntrianDetailController controller = loader.getController();
            controller.initData(antrian, this::loadAntrianData);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Detail Antrian #" + antrian.getId());
            stage.setScene(new Scene(parent));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(autoRefreshTimeline != null) autoRefreshTimeline.play();
    }
    
    private Color getStatusColor(String status) {
        return switch (status) {
            case "Baru" -> Color.DODGERBLUE;
            case "Sedang Berlangsung" -> Color.ORANGE;
            case "Selesai" -> Color.GREEN;
            case "Batal" -> Color.RED;
            default -> Color.GREY;
        };
    }

    protected abstract void loadAntrianData();
    
    @FXML
    protected void handleLogout() throws IOException {
        if (autoRefreshTimeline != null) {
            autoRefreshTimeline.stop();
        }
        Main.loggedInUser = null;
        new Main().changeScene("/fxml/Login.fxml", 600, 400);
    }
}