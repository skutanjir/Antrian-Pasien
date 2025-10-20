package com.antrian.ui.controller;

import com.antrian.core.model.User;
import com.antrian.core.service.FileService;
import com.antrian.ui.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class LoginController {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    @FXML
    protected void handleLoginButtonAction() {
        String emailOrName = emailField.getText();
        String password = passwordField.getText();

        if (emailOrName.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Email/Nama dan Password tidak boleh kosong.");
            return;
        }

        try {
            List<User> users = FileService.loadUsers();
            Optional<User> userOpt = users.stream()
                    .filter(u -> (u.getEmail().equalsIgnoreCase(emailOrName) || u.getNama().equalsIgnoreCase(emailOrName)) && u.getPassword().equals(password))
                    .findFirst();

            if (userOpt.isPresent()) {
                Main.loggedInUser = userOpt.get();
                Main main = new Main();
                if ("admin".equalsIgnoreCase(Main.loggedInUser.getRole())) {
                    main.changeScene("/fxml/AdminDashboard.fxml", 1024, 768);
                } else {
                    main.changeScene("/fxml/PatientDashboard.f xml", 1024, 768);
                }
            } else {
                errorLabel.setText("Email/Nama atau Password salah.");
            }
        } catch (IOException e) {
            errorLabel.setText("Gagal membaca data user.");
            e.printStackTrace();
        }
    }

    @FXML
    protected void goToRegister() throws IOException {
        new Main().changeScene("/fxml/Register.fxml", 600, 700);
    }
}