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

/**
 * Kelas {@code BuatAntrianController} mengatur logika pembuatan antrian baru oleh pasien.
 * <p>
 * Pengguna dapat memasukkan data diri, memilih poli tujuan, serta menulis keluhan.
 * Setelah data divalidasi dan disimpan, sistem akan menampilkan nomor antrian terbaru.
 * </p>
 *
 * <p>Fitur utama:</p>
 * <ul>
 *   <li>Validasi input agar semua field wajib diisi.</li>
 *   <li>Penentuan otomatis ID antrian berikutnya.</li>
 *   <li>Menyimpan data antrian baru ke file menggunakan {@link FileService}.</li>
 *   <li>Menampilkan pesan sukses atau error.</li>
 * </ul>
 *
 * @author
 *  Sulistyo Fajar Pratama,
 *  Dinda Diyah Arifa,
 *  Musthofa Agung Distyawan
 * @version 1.0
 * @since 2025
 */
public class BuatAntrianController {

    /** Field input NIK pasien. */
    @FXML private TextField nikField;
    /** Field input nama pasien. */
    @FXML private TextField namaField;
    /** Field input alamat pasien. */
    @FXML private TextField alamatField;
    /** Field input nomor telepon pasien. */
    @FXML private TextField noTeleponField;
    /** ComboBox untuk memilih poli tujuan. */
    @FXML private ComboBox<String> poliComboBox;
    /** Area teks untuk menulis keluhan pasien. */
    @FXML private TextArea keluhanArea;
    /** Label untuk menampilkan nomor antrian yang berhasil dibuat. */
    @FXML private Label infoNomorAntrian;

    /** Callback untuk memperbarui data pada dashboard setelah antrian dibuat. */
    private Runnable callback;

    /**
     * Mengatur callback yang akan dijalankan setelah antrian berhasil dibuat.
     *
     * @param callback fungsi yang dijalankan untuk memperbarui tampilan dashboard
     */
    public void setCallback(Runnable callback) {
        this.callback = callback;
    }

    /**
     * Inisialisasi awal tampilan dan pengisian daftar poli.
     */
    @FXML
    public void initialize() {
        poliComboBox.getItems().addAll("Poli Umum", "Poli Gigi", "Poli Anak", "Poli Jantung");
    }

    /**
     * Menangani aksi tombol "Ambil Antrian".
     * <p>Melakukan validasi, membuat objek antrian baru, menyimpannya ke file,
     * dan menampilkan pesan sukses atau error.</p>
     */
    @FXML
    private void handleAmbilAntrian() {
        // Validasi input wajib
        if (nikField.getText().isEmpty() ||
            namaField.getText().isEmpty() ||
            alamatField.getText().isEmpty() ||
            noTeleponField.getText().isEmpty() ||
            poliComboBox.getValue() == null ||
            keluhanArea.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Semua field wajib diisi.");
            return;
        }

        try {
            int nextId = FileService.getNextAntrianId();

            // Membuat objek antrian baru
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

            // Menyimpan ke file antrian
            List<Antrian> antrianList = FileService.loadAntrian();
            antrianList.add(newAntrian);
            FileService.saveAntrian(antrianList);

            // Menampilkan notifikasi sukses
            infoNomorAntrian.setText("Sukses! Anda mendapatkan nomor antrian: #" + nextId);
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Antrian berhasil dibuat dengan nomor #" + nextId);

            if (callback != null) {
                callback.run();
            }

            closeWindow();

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal menyimpan data antrian.");
            e.printStackTrace();
        }
    }

    /**
     * Menampilkan dialog pesan alert ke pengguna.
     *
     * @param alertType jenis alert (INFORMATION, ERROR, dll.)
     * @param title judul pesan
     * @param message isi pesan yang akan ditampilkan
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Menutup jendela form setelah proses selesai.
     */
    private void closeWindow() {
        Stage stage = (Stage) keluhanArea.getScene().getWindow();
        stage.close();
    }
}
