package com.antrian.ui.controller;

import com.antrian.core.model.Admin;
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

/**
 * Kelas {@code BaseDashboardController} merupakan kelas abstrak
 * yang menyediakan logika dan tampilan dasar untuk dashboard pengguna
 * (baik admin maupun pasien) dalam sistem antrian pasien.
 *
 * <p>Kelas ini mengatur:</p>
 * <ul>
 *   <li>Inisialisasi daftar antrian dan label sambutan.</li>
 *   <li>Filter berdasarkan nama pasien, poli, dan status.</li>
 *   <li>Auto-refresh data setiap 5 detik.</li>
 *   <li>Menampilkan tampilan kartu (card view) untuk setiap antrian.</li>
 * </ul>
 *
 * <p>Kelas ini digunakan sebagai induk untuk:
 * {@link AdminDashboardController} dan {@link PatientDashboardController}.</p>
 *
 * @author
 *  Sulistyo Fajar Pratama,
 *  Dinda Diyah Arifa,
 *  Musthofa Agung Distyawan
 * @version 1.0
 * @since 2025
 */
public abstract class BaseDashboardController {

    /** Daftar antrian yang akan ditampilkan di dashboard. */
    @FXML protected ListView<Antrian> antrianListView;
    /** Label untuk menampilkan nama pengguna yang sedang login. */
    @FXML protected Label welcomeLabel;
    /** Kolom pencarian berdasarkan nama pasien. */
    @FXML protected TextField searchField;
    /** ComboBox untuk memilih filter poli. */
    @FXML protected ComboBox<String> poliFilterComboBox;
    /** ComboBox untuk memilih filter status. */
    @FXML protected ComboBox<String> statusFilterComboBox;

    /** Daftar penuh data antrian sebelum difilter. */
    protected ObservableList<Antrian> fullAntrianList = FXCollections.observableArrayList();
    /** Timeline untuk auto-refresh data dashboard. */
    private Timeline autoRefreshTimeline;

    /**
     * Inisialisasi awal tampilan dashboard, termasuk label sambutan,
     * filter, daftar antrian, dan auto-refresh.
     */
    @FXML
    public void initialize() {
        welcomeLabel.setText("Selamat Datang, " + Main.loggedInUser.getNama() + "!");
        setupFilterControls();
        setupListViewFactory();
        loadAntrianData();
        startAutoRefresh();
    }

    /**
     * Memulai auto-refresh dashboard setiap 5 detik agar data selalu terbaru.
     */
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

    /**
     * Mengatur kontrol filter untuk poli, status, dan pencarian.
     * Saat filter berubah, daftar antrian diperbarui otomatis.
     */
    private void setupFilterControls() {
        poliFilterComboBox.getItems().addAll("Semua Poli", "Poli Umum", "Poli Gigi", "Poli Anak", "Poli Jantung");
        poliFilterComboBox.setValue("Semua Poli");
        statusFilterComboBox.getItems().addAll("Semua Status", "Baru", "Sedang Berlangsung", "Selesai", "Batal");
        statusFilterComboBox.setValue("Semua Status");

        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        poliFilterComboBox.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        statusFilterComboBox.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
    }

    /**
     * Menerapkan filter pada daftar antrian berdasarkan:
     * <ul>
     *   <li>Nama pasien</li>
     *   <li>Poli</li>
     *   <li>Status antrian</li>
     * </ul>
     */
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

        // mempertahankan pilihan sebelumnya
        if (selected != null && antrianListView.getItems().contains(selected)) {
            antrianListView.getSelectionModel().select(selected);
        }
    }

    /**
     * Mengatur tampilan ListView agar setiap item antrian
     * ditampilkan dalam bentuk kartu dengan warna status berbeda.
     */
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

                    // tombol detail hanya muncul jika pengguna adalah admin
                    if (Main.loggedInUser instanceof Admin) {
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

    /**
     * Menampilkan jendela detail antrian dalam dialog modal.
     * Digunakan terutama oleh admin untuk melihat dan mengedit data.
     *
     * @param antrian Objek antrian yang dipilih
     */
    protected void showDetailAntrian(Antrian antrian) {
        if (autoRefreshTimeline != null) autoRefreshTimeline.pause();
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
        if (autoRefreshTimeline != null) autoRefreshTimeline.play();
    }

    /**
     * Mengembalikan warna indikator status pada tampilan kartu antrian.
     *
     * @param status status antrian
     * @return warna indikator sesuai status
     */
    private Color getStatusColor(String status) {
        return switch (status) {
            case "Baru" -> Color.DODGERBLUE;
            case "Sedang Berlangsung" -> Color.ORANGE;
            case "Selesai" -> Color.GREEN;
            case "Batal" -> Color.RED;
            default -> Color.GREY;
        };
    }

    /**
     * Method abstrak untuk memuat data antrian dari file.
     * Setiap subclass harus mengimplementasikan metode ini.
     */
    protected abstract void loadAntrianData();

    /**
     * Menangani proses logout pengguna dan kembali ke halaman login.
     *
     * @throws IOException jika file FXML halaman login tidak ditemukan
     */
    @FXML
    protected void handleLogout() throws IOException {
        if (autoRefreshTimeline != null) {
            autoRefreshTimeline.stop();
        }
        Main.loggedInUser = null;
        new Main().changeScene("/fxml/Login.fxml", 600, 400);
    }
}
