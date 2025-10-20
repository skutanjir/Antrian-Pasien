package com.antrian.ui.controller;

import com.antrian.core.model.User;
import com.antrian.core.model.Admin;
import com.antrian.core.model.Pasien;
import com.antrian.core.service.FileService;
import com.antrian.ui.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Kelas {@code LoginController} mengatur logika autentikasi pengguna
 * berdasarkan data yang tersimpan di file pengguna (users.txt).
 * <p>
 * Sistem akan mengenali apakah pengguna adalah Admin atau Pasien,
 * kemudian mengarahkan ke dashboard yang sesuai.
 * </p>
 *
 * <p>Fitur utama:</p>
 * <ul>
 *   <li>Validasi input email/nama dan password.</li>
 *   <li>Autentikasi data dari file menggunakan {@link FileService}.</li>
 *   <li>Redirect otomatis ke dashboard sesuai peran (Admin atau Pasien).</li>
 * </ul>
 *
 * @author
 *  Sulistyo Fajar Pratama,
 *  Dinda Diyah Arifa,
 *  Musthofa Agung Distyawan
 * @version 1.0
 * @since 2025
 */
public class LoginController {

    /** Field input email atau nama pengguna. */
    @FXML private TextField emailField;
    /** Field input password pengguna. */
    @FXML private PasswordField passwordField;
    /** Label untuk menampilkan pesan kesalahan login. */
    @FXML private Label errorLabel;

    /**
     * Menangani aksi tombol login. Melakukan validasi dan autentikasi pengguna
     * berdasarkan input email/nama dan password.
     */
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
                    .filter(u -> (u.getEmail().equalsIgnoreCase(emailOrName)
                            || u.getNama().equalsIgnoreCase(emailOrName))
                            && u.getPassword().equals(password))
                    .findFirst();

            if (userOpt.isPresent()) {
                Main.loggedInUser = userOpt.get();
                Main main = new Main();

                if (Main.loggedInUser instanceof Admin) {
                    main.changeScene("/fxml/AdminDashboard.fxml", 1024, 768);
                } else if (Main.loggedInUser instanceof Pasien) {
                    main.changeScene("/fxml/PatientDashboard.fxml", 1024, 768);
                } else {
                    errorLabel.setText("Tipe pengguna tidak dikenali.");
                }

            } else {
                errorLabel.setText("Email/Nama atau Password salah.");
            }
        } catch (IOException e) {
            errorLabel.setText("Gagal membaca data user.");
            e.printStackTrace();
        }
    }

    /**
     * Mengalihkan pengguna ke halaman registrasi.
     *
     * @throws IOException jika file FXML Register.fxml tidak ditemukan
     */
    @FXML
    protected void goToRegister() throws IOException {
        new Main().changeScene("/fxml/Register.fxml", 600, 700);
    }
}
