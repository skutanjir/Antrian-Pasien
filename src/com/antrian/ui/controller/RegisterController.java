package com.antrian.ui.controller;

import com.antrian.core.model.User;
import com.antrian.core.service.FileService;
import com.antrian.ui.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RegisterController {

    @FXML private TextField nikField;
    @FXML private TextField namaField;
    @FXML private TextField alamatField;
    @FXML private TextField noTeleponField;
    @FXML private TextField emailField;
    @FXML private DatePicker tanggalLahirPicker;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    protected void handleDaftarButtonAction() {
        if (nikField.getText().isEmpty() || namaField.getText().isEmpty() || alamatField.getText().isEmpty() || noTeleponField.getText().isEmpty() || emailField.getText().isEmpty() || tanggalLahirPicker.getValue() == null || passwordField.getText().isEmpty()) {
            errorLabel.setText("Semua field harus diisi.");
            return;
        }

        String tanggalLahir = tanggalLahirPicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        User newUser = new User(
                nikField.getText(),
                namaField.getText(),
                alamatField.getText(),
                noTeleponField.getText(),
                emailField.getText(),
                tanggalLahir,
                passwordField.getText(),
                "pasien"
        );

        try {
            List<User> users = FileService.loadUsers();
            boolean userExists = users.stream().anyMatch(u -> u.getNik().equals(newUser.getNik()) || u.getEmail().equals(newUser.getEmail()));
            if (userExists) {
                errorLabel.setText("NIK atau Email sudah terdaftar.");
                return;
            }

            users.add(newUser);
            FileService.saveUsers(users);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sukses");
            alert.setHeaderText(null);
            alert.setContentText("Registrasi berhasil! Silakan login.");
            alert.showAndWait();

            new Main().changeScene("/fxml/Login.fxml", 600, 400);

        } catch (IOException e) {
            errorLabel.setText("Gagal menyimpan data user.");
            e.printStackTrace();
        }
    }
    
    @FXML
    protected void goToLogin() throws IOException {
        new Main().changeScene("/fxml/Login.fxml", 600, 400);
    }
}