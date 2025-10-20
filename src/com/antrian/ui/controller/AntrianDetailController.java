package com.antrian.ui.controller;

import com.antrian.core.model.Antrian;
import com.antrian.core.service.FileService;
import com.antrian.ui.Main;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class AntrianDetailController {

    @FXML private Label antrianIdLabel;
    @FXML private TextField nikField;
    @FXML private TextField namaField;
    @FXML private TextField alamatField;
    @FXML private TextField noTeleponField;
    @FXML private TextField poliField;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private TextArea keluhanArea;
    @FXML private Button hapusButton;
    @FXML private Button simpanButton;

    private Antrian currentAntrian;
    private Runnable refreshCallback;
    private Timeline autoRefreshTimeline;

    public void initData(Antrian antrian, Runnable callback) {
        this.currentAntrian = antrian;
        this.refreshCallback = callback;
        
        populateFields(antrian);
        
        boolean isAdmin = "admin".equalsIgnoreCase(Main.loggedInUser.getRole());
        statusComboBox.setDisable(!isAdmin);
        simpanButton.setVisible(isAdmin);
        simpanButton.setManaged(isAdmin);
        hapusButton.setVisible(isAdmin);
        hapusButton.setManaged(isAdmin);
        
        startAutoRefresh();
    }

    private void populateFields(Antrian antrian) {
        antrianIdLabel.setText("Antrian #" + antrian.getId());
        nikField.setText(antrian.getNikPasien());
        namaField.setText(antrian.getNamaPasien());
        alamatField.setText(antrian.getAlamatPasien());
        noTeleponField.setText(antrian.getNoTeleponPasien());
        poliField.setText(antrian.getPoli());
        keluhanArea.setText(antrian.getKeluhan());
        
        if (statusComboBox.getItems().isEmpty()) {
            statusComboBox.getItems().addAll("Baru", "Sedang Berlangsung", "Selesai", "Batal");
        }
        statusComboBox.setValue(antrian.getStatus());
    }

    private void startAutoRefresh() {
        autoRefreshTimeline = new Timeline(
            new KeyFrame(Duration.seconds(3), event -> {
                System.out.println("Refreshing detail view for Antrian #" + currentAntrian.getId());
                refreshData();
            })
        );
        autoRefreshTimeline.setCycleCount(Timeline.INDEFINITE);
        autoRefreshTimeline.play();
    }

    private void refreshData() {
        try {
            Optional<Antrian> updatedAntrianOpt = FileService.loadAndProcessAntrianStatus().stream()
                .filter(a -> a.getId() == currentAntrian.getId())
                .findFirst();
            
            if (updatedAntrianOpt.isPresent()) {
                this.currentAntrian = updatedAntrianOpt.get();
                populateFields(this.currentAntrian);
            } else {
                closeWindow();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSimpan() {
        try {
            List<Antrian> antrianList = FileService.loadAntrian();
            antrianList.stream()
                .filter(a -> a.getId() == currentAntrian.getId())
                .findFirst()
                .ifPresent(antrianToUpdate -> antrianToUpdate.setStatus(statusComboBox.getValue()));
            FileService.saveAntrian(antrianList);
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data antrian berhasil diperbarui.");
            closeWindow();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal menyimpan data.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleHapus() {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Anda yakin ingin menghapus antrian ini?", ButtonType.YES, ButtonType.NO);
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    List<Antrian> antrianList = FileService.loadAntrian();
                    antrianList.removeIf(a -> a.getId() == currentAntrian.getId());
                    FileService.saveAntrian(antrianList);
                    showAlert(Alert.AlertType.INFORMATION, "Sukses", "Antrian berhasil dihapus.");
                    closeWindow();
                } catch (IOException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Gagal menghapus data.");
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void handleKembali() {
        closeWindow();
    }
    
    private void closeWindow() {
        if(autoRefreshTimeline != null) {
            autoRefreshTimeline.stop();
        }
        if(refreshCallback != null) {
            refreshCallback.run();
        }
        Stage stage = (Stage) simpanButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}