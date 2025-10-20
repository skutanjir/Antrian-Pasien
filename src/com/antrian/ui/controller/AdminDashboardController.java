package com.antrian.ui.controller;

import com.antrian.core.model.Antrian;
import com.antrian.core.service.FileService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;

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
 * <p>Admin dapat melakukan:
 * <ul>
 *   <li>Melihat seluruh data antrian pasien.</li>
 *   <li>Menyaring data berdasarkan nama, poli, status, dan waktu (minggu/bulan/tahun).</li>
 *   <li>Mereset seluruh data antrian melalui konfirmasi.</li>
 * </ul>
 * </p>
 *
 * <p>Kelas ini mewarisi dari {@link BaseDashboardController} untuk menggunakan fitur dasar
 * seperti daftar antrian, pencarian, dan filter poli/status.</p>
 *
 * @author
 *  Sulistyo Fajar Pratama,
 *  Dinda Diyah Arifa,
 *  Musthofa Agung Distyawan
 * @version 1.0
 * @since 2025
 */
public class AdminDashboardController extends BaseDashboardController {

    /** Dropdown filter waktu (Semua Waktu, Minggu Ini, Bulan Ini, Tahun Ini). */
    @FXML
    private ComboBox<String> dateFilterComboBox;

    /**
     * Inisialisasi awal tampilan dashboard admin.
     * Menambahkan opsi filter waktu dan mendengarkan perubahan nilai filter.
     */
    @Override
    public void initialize() {
        super.initialize();
        dateFilterComboBox.getItems().addAll("Semua Waktu", "Minggu Ini", "Bulan Ini", "Tahun Ini");
        dateFilterComboBox.setValue("Semua Waktu");
        dateFilterComboBox.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
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

        // mempertahankan pilihan item sebelumnya jika masih ada
        if (selected != null && antrianListView.getItems().contains(selected)) {
            antrianListView.getSelectionModel().select(selected);
        }
    }

    /**
     * Menangani aksi tombol "Reset Antrian" untuk menghapus seluruh data antrian.
     * Akan muncul dialog konfirmasi sebelum proses penghapusan dilakukan.
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
                loadAntrianData();
            } catch (IOException e) {
                e.printStackTrace();
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setContentText("Gagal mereset data antrian.");
                errorAlert.showAndWait();
            }
        }
    }
}
