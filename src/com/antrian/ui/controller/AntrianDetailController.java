package com.antrian.ui.controller;

import com.antrian.core.model.Admin;
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

/**
 * Kelas {@code AntrianDetailController} mengatur tampilan dan logika untuk
 * menampilkan detail data antrian pasien pada aplikasi.
 *
 * <p>Admin dapat memperbarui atau menghapus antrian, sedangkan pasien hanya dapat
 * melihat detail antriannya tanpa mengubah data.</p>
 *
 * <p>Fitur utama:</p>
 * <ul>
 *   <li>Menampilkan detail lengkap antrian (data pasien, poli, status, keluhan).</li>
 *   <li>Auto-refresh setiap 3 detik agar status antrian selalu up to date.</li>
 *   <li>Admin dapat menyimpan perubahan status atau menghapus antrian.</li>
 * </ul>
 *
 * @author
 *  Sulistyo Fajar Pratama,
 *  Dinda Diyah Arifa,
 *  Musthofa Agung Distyawan
 * @version 1.0
 * @since 2025
 */
public class AntrianDetailController {

    /** Label untuk menampilkan ID antrian. */
    @FXML private Label antrianIdLabel;
    /** Field teks NIK pasien. */
    @FXML private TextField nikField;
    /** Field teks nama pasien. */
    @FXML private TextField namaField;
    /** Field teks alamat pasien. */
    @FXML private TextField alamatField;
    /** Field teks nomor telepon pasien. */
    @FXML private TextField noTeleponField;
    /** Field teks poli tujuan pasien. */
    @FXML private TextField poliField;
    /** ComboBox untuk memilih atau menampilkan status antrian. */
    @FXML private ComboBox<String> statusComboBox;
    /** Area teks untuk menampilkan keluhan pasien. */
    @FXML private TextArea keluhanArea;
    /** Tombol untuk menghapus antrian (hanya admin). */
    @FXML private Button hapusButton;
    /** Tombol untuk menyimpan perubahan status antrian (hanya admin). */
    @FXML private Button simpanButton;

    /** Objek antrian yang sedang ditampilkan. */
    private Antrian currentAntrian;
    /** Callback untuk memperbarui tampilan utama setelah perubahan. */
    private Runnable refreshCallback;
    /** Timeline otomatis untuk memperbarui tampilan detail antrian. */
    private Timeline autoRefreshTimeline;

    /**
     * Inisialisasi data antrian yang akan ditampilkan pada tampilan detail.
     *
     * @param antrian Objek antrian yang dipilih
     * @param callback Fungsi callback untuk memperbarui tampilan utama setelah aksi
     */
    public void initData(Antrian antrian, Runnable callback) {
        this.currentAntrian = antrian;
        this.refreshCallback = callback;
        populateFields(antrian);

        boolean isAdmin = Main.loggedInUser instanceof Admin;
        statusComboBox.setDisable(!isAdmin);
        simpanButton.setVisible(isAdmin);
        simpanButton.setManaged(isAdmin);
        hapusButton.setVisible(isAdmin);
        hapusButton.setManaged(isAdmin);

        startAutoRefresh();
    }

    /**
     * Mengisi seluruh field pada tampilan dengan data dari objek antrian.
     *
     * @param antrian Objek antrian yang datanya akan ditampilkan
     */
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

    /**
     * Memulai pembaruan otomatis tampilan detail antrian setiap 3 detik.
     */
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

    /**
     * Memuat ulang data antrian dari file untuk memastikan status selalu mutakhir.
     */
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

    /**
     * Menangani aksi tombol simpan (khusus admin) untuk memperbarui status antrian.
     */
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

    /**
     * Menangani aksi tombol hapus (khusus admin) untuk menghapus antrian.
     * Menampilkan dialog konfirmasi sebelum menghapus data.
     */
    @FXML
    private void handleHapus() {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION,
                "Anda yakin ingin menghapus antrian ini?", ButtonType.YES, ButtonType.NO);
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

    /**
     * Menangani aksi tombol kembali untuk menutup jendela detail.
     */
    @FXML
    private void handleKembali() {
        closeWindow();
    }

    /**
     * Menutup jendela detail antrian dan menghentikan pembaruan otomatis.
     * Juga memanggil callback untuk memperbarui tampilan utama.
     */
    private void closeWindow() {
        if (autoRefreshTimeline != null) {
            autoRefreshTimeline.stop();
        }
        if (refreshCallback != null) {
            refreshCallback.run();
        }
        Stage stage = (Stage) simpanButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Menampilkan pesan informasi atau error kepada pengguna.
     *
     * @param type Jenis alert (INFORMATION, ERROR, dll.)
     * @param title Judul pesan
     * @param content Isi pesan yang akan ditampilkan
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
