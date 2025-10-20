package com.antrian.ui.controller;

import com.antrian.core.model.Antrian;
import com.antrian.core.service.FileService;
import com.antrian.ui.Main;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class BuatAntrianController {

    @FXML private TextField nikField;
    @FXML private TextField namaField;
    @FXML private TextField alamatField;
    @FXML private TextField noTeleponField;
    @FXML private ComboBox<String> poliComboBox;
    @FXML private TextArea keluhanArea;
    @FXML private Label infoNomorAntrian;

    private Runnable callback;

    public void setCallback(Runnable callback) {
        this.callback = callback;
    }

    @FXML
    public void initialize() {
        poliComboBox.getItems().addAll("Poli Umum", "Poli Gigi", "Poli Anak", "Poli Jantung");
    }

    @FXML
    private void handleAmbilAntrian() {
        if (nikField.getText().isEmpty() || namaField.getText().isEmpty() || alamatField.getText().isEmpty() || noTeleponField.getText().isEmpty() || poliComboBox.getValue() == null || keluhanArea.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Semua field wajib diisi.");
            return;
        }

        try {
            int nextId = FileService.getNextAntrianId();

            // --- LOGIKA BARU PEMBUATAN ANTRIAN ---
            Antrian newAntrian = new Antrian(
                nextId,
                Main.loggedInUser.getNik(),
                nikField.getText(),
                namaField.getText(),
                alamatField.getText(),
                noTeleponField.getText(),
                poliComboBox.getValue(),
                keluhanArea.getText(),
                "Baru",
                LocalDateTime.now()
            );

            List<Antrian> antrianList = FileService.loadAntrian();
            antrianList.add(newAntrian);
            FileService.saveAntrian(antrianList);

            infoNomorAntrian.setText("Sukses! Anda mendapatkan nomor antrian: #" + nextId);
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Antrian berhasil dibuat dengan nomor #" + nextId);
            
            if(callback != null) {
                callback.run();
            }
            closeWindow();

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal menyimpan data antrian.");
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void closeWindow() {
        Stage stage = (Stage) keluhanArea.getScene().getWindow();
        stage.close();
    }
}