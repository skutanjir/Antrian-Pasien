package com.antrian.ui.controller;

import com.antrian.core.model.Pasien;
import com.antrian.core.model.User;
import com.antrian.core.service.FileService;
import com.antrian.ui.Main;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Kelas {@code RegisterController} mengatur proses registrasi akun baru
 * untuk pengguna dengan peran pasien.
 * <p>
 * Pengguna diminta mengisi data pribadi lengkap seperti NIK, nama, alamat,
 * nomor telepon, email, tanggal lahir, dan password. Setelah semua data valid,
 * sistem akan menyimpannya ke file pengguna.
 * </p>
 *
 * <p>Fitur utama:</p>
 * <ul>
 *   <li>Validasi input agar semua field wajib diisi.</li>
 *   <li>Pengecekan duplikasi NIK atau email sebelum menyimpan.</li>
 *   <li>Penyimpanan data pasien baru menggunakan {@link FileService}.</li>
 *   <li>Redirect otomatis ke halaman login setelah registrasi sukses.</li>
 * </ul>
 *
 * @author
 *  Sulistyo Fajar Pratama,
 *  Dinda Diyah Arifa,
 *  Musthofa Agung Distyawan
 * @version 1.0
 * @since 2025
 */
public class RegisterController {

    /** Field input NIK pasien. */
    @FXML private TextField nikField;
    /** Field input nama pasien. */
    @FXML private TextField namaField;
    /** Field input alamat pasien. */
    @FXML private TextField alamatField;
    /** Field input nomor telepon pasien. */
    @FXML private TextField noTeleponField;
    /** Field input email pasien. */
    @FXML private TextField emailField;
    /** DatePicker untuk memilih tanggal lahir pasien. */
    @FXML private DatePicker tanggalLahirPicker;
    /** Field input password pasien. */
    @FXML private PasswordField passwordField;
    /** Label untuk menampilkan pesan kesalahan. */
    @FXML private Label errorLabel;

    /**
     * Menangani aksi tombol "Daftar".
     * <p>Memvalidasi input pengguna, membuat objek pasien baru, dan menyimpannya ke file.</p>
     */
    @FXML
    protected void handleDaftarButtonAction() {
        // Validasi input
        if (nikField.getText().isEmpty() || namaField.getText().isEmpty() ||
            alamatField.getText().isEmpty() || noTeleponField.getText().isEmpty() ||
            emailField.getText().isEmpty() || tanggalLahirPicker.getValue() == null ||
            passwordField.getText().isEmpty()) {

            errorLabel.setText("Semua field harus diisi.");
            return;
        }

        // Format tanggal lahir ke dd/MM/yyyy
        String tanggalLahir = tanggalLahirPicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // Membuat objek pasien baru
        User newUser = new Pasien(
                nikField.getText(),
                namaField.getText(),
                alamatField.getText(),
                noTeleponField.getText(),
                emailField.getText(),
                tanggalLahir,
                passwordField.getText(),
                "RM-" + nikField.getText()
        );

        try {
            List<User> users = FileService.loadUsers();

            // Cek duplikasi NIK atau email
            boolean userExists = users.stream()
                    .anyMatch(u -> u.getNik().equals(newUser.getNik()) || u.getEmail().equals(newUser.getEmail()));

            if (userExists) {
                errorLabel.setText("NIK atau Email sudah terdaftar.");
                return;
            }

            // Simpan pengguna baru
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

    /**
     * Mengalihkan pengguna ke halaman login.
     *
     * @throws IOException jika file FXML Login.fxml tidak ditemukan
     */
    @FXML
    protected void goToLogin() throws IOException {
        new Main().changeScene("/fxml/Login.fxml", 600, 400);
    }
}
