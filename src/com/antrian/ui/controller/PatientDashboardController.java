package com.antrian.ui.controller;

import com.antrian.core.model.Antrian;
import com.antrian.core.service.FileService;
import com.antrian.ui.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Kelas {@code PatientDashboardController} mengatur tampilan dan logika dashboard
 * untuk pengguna dengan peran pasien.
 * <p>
 * Pasien dapat melihat riwayat antrian yang mereka buat, melakukan penyaringan data,
 * serta menambahkan antrian baru melalui form pendaftaran.
 * </p>
 *
 * <p>Fitur utama:</p>
 * <ul>
 *   <li>Menampilkan daftar antrian yang dibuat oleh pasien yang sedang login.</li>
 *   <li>Melakukan pembaruan otomatis terhadap status antrian.</li>
 *   <li>Membuka form pembuatan antrian baru.</li>
 * </ul>
 *
 * <p>Kelas ini merupakan turunan dari {@link BaseDashboardController}.</p>
 *
 * @author
 *  Sulistyo Fajar Pratama,
 *  Dinda Diyah Arifa,
 *  Musthofa Agung Distyawan
 * @version 1.0
 * @since 2025
 */
public class PatientDashboardController extends BaseDashboardController {

    /**
     * Memuat data antrian khusus untuk pasien yang sedang login.
     * Data akan difilter berdasarkan NIK pembuat yang sesuai
     * dengan pengguna saat ini.
     */
    @Override
    protected void loadAntrianData() {
        try {
            List<Antrian> allDataForPatient = FileService.loadAndProcessAntrianStatus().stream()
                    .filter(a -> a.getNikPembuat().equals(Main.loggedInUser.getNik()))
                    .collect(Collectors.toList());
            fullAntrianList.setAll(allDataForPatient);
            applyFilters();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Menangani aksi tombol "Buat Antrian".
     * <p>Membuka jendela form baru untuk membuat antrian dan
     * memperbarui tampilan dashboard setelah data tersimpan.</p>
     */
    @FXML
    private void handleBuatAntrian() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/BuatAntrian.fxml"));
            Parent parent = loader.load();

            BuatAntrianController controller = loader.getController();
            controller.setCallback(this::loadAntrianData);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Buat Antrian Baru");
            stage.setScene(new Scene(parent));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
