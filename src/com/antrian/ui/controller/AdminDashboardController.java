package com.antrian.ui.controller;

import com.antrian.core.model.Antrian;
import com.antrian.core.service.FileService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox; // Import VBox (atau layout panel detail Anda)

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Kelas {@code AdminDashboardController} mengatur perilaku dan logika tampilan dashboard
 * untuk pengguna dengan peran admin dalam sistem antrian pasien.
 *
 * <p>Admin dapat:
 * <ul>
 * <li>Melihat seluruh data antrian pasien.</li>
 * <li>Menyaring data berdasarkan nama, poli, status, dan waktu.</li>
 * <li>Mereset seluruh data antrian melalui konfirmasi.</li>
 * <li><b>Mengedit dan menghapus antrian langsung dari dashboard.</b></li>
 * </ul>
 * </p>
 *
 * @author
 * Sulistyo Fajar Pratama,
 * Dinda Diyah Arifa,
 * Musthofa Agung Distyawan
 * @version 1.1 (Modifikasi: Gabung fungsionalitas AntrianDetailController)
 * @since 2025
 */
public class AdminDashboardController extends BaseDashboardController {

    /** Dropdown filter waktu (Semua Waktu, Minggu Ini, Bulan Ini, Tahun Ini). */
    @FXML
    private ComboBox<String> dateFilterComboBox;

    // ==========================================================
    // == KOMPONEN FXML DARI AntrianDetailController ==
    // =GANDA=PENTING: Anda HARUS menambahkan komponen ini di file FXML Anda!
    // ==========================================================

    /** Panel/layout yang berisi semua form detail, disembunyikan jika tidak ada item dipilih. */
    @FXML private VBox detailPane; // Asumsi Anda menggunakan VBox dengan fx:id="detailPane"

    @FXML private Label antrianIdLabel;
    @FXML private TextField nikField;
    @FXML private TextField namaField;
    @FXML private TextField alamatField;
    @FXML private TextField noTeleponField;
    @FXML private TextField poliField;
    @FXML private ComboBox<String> statusComboBoxDetail; // Ganti nama agar tidak konflik
    @FXML private TextArea keluhanArea;
    @FXML private Button hapusButton;
    @FXML private Button simpanButton;
    @FXML private Button batalButton; // Tombol baru untuk membatalkan pilihan

    // ======================
    // == CLASS VARIABLES  ==
    // ======================
    
    /** Menyimpan referensi ke antrian yang sedang dipilih di form detail. */
    private Antrian currentAntrian;

    // ======================
    // == INITIALIZATION   ==
    // ======================

    /**
     * Inisialisasi awal tampilan dashboard admin.
     * Menambahkan opsi filter waktu dan mendengarkan perubahan nilai filter.
     * Juga menambahkan listener untuk ListView untuk menampilkan panel detail.
     */
    @Override
    public void initialize() {
        super.initialize(); // Menjalankan initialize dari BaseDashboardController
        
        // Setup filter tanggal
        dateFilterComboBox.getItems().addAll("Semua Waktu", "Minggu Ini", "Bulan Ini", "Tahun Ini");
        dateFilterComboBox.setValue("Semua Waktu");
        dateFilterComboBox.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());

        // --- Logic Merged from AntrianDetailController ---
        
        // 1. Sembunyikan panel detail di awal
        if (detailPane != null) { // Cek null untuk keamanan jika FXML belum diupdate
            detailPane.setVisible(false);
            detailPane.setManaged(false);
        }

        // 2. Setup status combo box di panel detail
        if (statusComboBoxDetail != null) {
            statusComboBoxDetail.getItems().addAll("Baru", "Sedang Berlangsung", "Selesai", "Batal");
        }

        // 3. Setup ListView selection listener
        antrianListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            this.currentAntrian = newSelection; // Simpan antrian yang dipilih

            if (newSelection != null) {
                // Jika item dipilih, isi data dan tampilkan panel
                populateFields(newSelection);
                if (detailPane != null) {
                    detailPane.setVisible(true);
                    detailPane.setManaged(true);
                }
            } else {
                // Jika pilihan dibatalkan, sembunyikan panel
                if (detailPane != null) {
                    detailPane.setVisible(false);
                    detailPane.setManaged(false);
                }
            }
        });
    }

    /**
     * Memuat seluruh data antrian dari file dan memperbarui tampilan daftar.
     */
    @Override
    protected void loadAntrianData() {
        try {
            List<Antrian> allData = FileService.loadAndProcessAntrianStatus();
            fullAntrianList.setAll(allData);
            applyFilters();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Menerapkan seluruh filter yang dipilih (nama, poli, status, dan waktu).
     * Hasil filter akan ditampilkan di {@code antrianListView}.
     */
    @Override
    protected void applyFilters() {
        Antrian selected = antrianListView.getSelectionModel().getSelectedItem();

        var filteredList = fullAntrianList.stream()
                // filter berdasarkan nama pasien
                .filter(antrian -> {
                    String searchText = searchField.getText().toLowerCase();
                    if (searchText.isEmpty()) return true;
                    return antrian.getNamaPasien().toLowerCase().contains(searchText);
                })
                // filter berdasarkan poli
                .filter(antrian -> {
                    String selectedPoli = poliFilterComboBox.getValue();
                    if (selectedPoli == null || selectedPoli.equals("Semua Poli")) return true;
                    return antrian.getPoli().equals(selectedPoli);
                })
                // filter berdasarkan status
                .filter(antrian -> {
                    String selectedStatus = statusFilterComboBox.getValue();
                    if (selectedStatus == null || selectedStatus.equals("Semua Status")) return true;
                    return antrian.getStatus().equals(selectedStatus);
                })
                // filter berdasarkan waktu
                .filter(antrian -> {
                    String selectedDate = dateFilterComboBox.getValue();
                    if (selectedDate == null || selectedDate.equals("Semua Waktu")) return true;

                    LocalDate antrianDate = antrian.getTimestamp().toLocalDate();
                    LocalDate now = LocalDate.now();

                    return switch (selectedDate) {
                        case "Minggu Ini" ->
                                antrianDate.get(WeekFields.ISO.weekOfYear()) == now.get(WeekFields.ISO.weekOfYear())
                                        && antrianDate.getYear() == now.getYear();
                        case "Bulan Ini" ->
                                antrianDate.getMonth() == now.getMonth()
                                        && antrianDate.getYear() == now.getYear();
                        case "Tahun Ini" ->
                                antrianDate.getYear() == now.getYear();
                        default -> true;
                    };
                })
                .collect(Collectors.toList());

        antrianListView.setItems(FXCollections.observableArrayList(filteredList));

        // Mempertahankan pilihan item sebelumnya jika masih ada di list yang baru difilter
        if (selected != null && antrianListView.getItems().contains(selected)) {
            antrianListView.getSelectionModel().select(selected);
        } else {
            // Jika item yang dipilih hilang (karena filter), bersihkan pilihan
            antrianListView.getSelectionModel().clearSelection();
            // Listener di initialize() akan otomatis menyembunyikan detailPane
        }
    }

    /**
     * Menangani aksi tombol "Reset Antrian" untuk menghapus seluruh data antrian.
     */
    @FXML
    private void handleResetAntrian() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Konfirmasi Reset");
        confirmation.setHeaderText("Reset Semua Data Antrian");
        confirmation.setContentText("Anda yakin ingin menghapus SEMUA data antrian? Tindakan ini tidak bisa dibatalkan.");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                FileService.clearAntrianFile();
                loadAntrianData(); // Muat ulang (akan jadi kosong)
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Gagal mereset data antrian.");
            }
        }
    }

    /**
     * Mengisi seluruh field pada panel detail dengan data dari objek antrian yang dipilih.
     *
     * @param antrian Objek antrian yang datanya akan ditampilkan
     */
    private void populateFields(Antrian antrian) {
        antrianIdLabel.setText("Detail Antrian #" + antrian.getId());
        nikField.setText(antrian.getNikPasien());
        namaField.setText(antrian.getNamaPasien());
        alamatField.setText(antrian.getAlamatPasien());
        noTeleponField.setText(antrian.getNoTeleponPasien());
        poliField.setText(antrian.getPoli());
        keluhanArea.setText(antrian.getKeluhan());
        statusComboBoxDetail.setValue(antrian.getStatus());
    }

    /**
     * Menangani aksi tombol simpan untuk memperbarui status antrian.
     */
    @FXML
    private void handleSimpan() {
        if (currentAntrian == null) return;

        try {
            List<Antrian> antrianList = FileService.loadAntrian();
            antrianList.stream()
                .filter(a -> a.getId() == currentAntrian.getId())
                .findFirst()
                .ifPresent(a -> a.setStatus(statusComboBoxDetail.getValue()));

            FileService.saveAntrian(antrianList);
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data antrian berhasil diperbarui.");
            loadAntrianData(); 
            
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal menyimpan data.");
            e.printStackTrace();
        }
    }

    /**
     * Menangani aksi tombol hapus untuk menghapus antrian yang dipilih.
     */
    @FXML
    private void handleHapus() {
        if (currentAntrian == null) return;
        
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION,
                "Anda yakin ingin menghapus antrian #" + currentAntrian.getId() + " (" + currentAntrian.getNamaPasien() + ")?",
                ButtonType.YES, ButtonType.NO);

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    List<Antrian> antrianList = FileService.loadAntrian();
                    antrianList.removeIf(a -> a.getId() == currentAntrian.getId());
                    FileService.saveAntrian(antrianList);
                    showAlert(Alert.AlertType.INFORMATION, "Sukses", "Antrian berhasil dihapus.");
                    
                    // Muat ulang data. Pilihan akan otomatis hilang (clearSelection)
                    // dan panel detail akan tersembunyi.
                    loadAntrianData(); 
                    
                } catch (IOException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Gagal menghapus data.");
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Menangani aksi tombol batal untuk membersihkan pilihan.
     */
    @FXML
    private void handleBatal() {
        antrianListView.getSelectionModel().clearSelection();
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