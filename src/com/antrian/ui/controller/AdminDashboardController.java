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

public class AdminDashboardController extends BaseDashboardController {

    @FXML
    private ComboBox<String> dateFilterComboBox;

    @Override
    public void initialize() {
        super.initialize();
        dateFilterComboBox.getItems().addAll("Semua Waktu", "Minggu Ini", "Bulan Ini", "Tahun Ini");
        dateFilterComboBox.setValue("Semua Waktu");
        dateFilterComboBox.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
    }

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

    @Override
    protected void applyFilters() {
        Antrian selected = antrianListView.getSelectionModel().getSelectedItem();
        var filteredList = fullAntrianList.stream()
                .filter(antrian -> {
                    String searchText = searchField.getText().toLowerCase();
                    if (searchText.isEmpty()) return true;
                    return antrian.getNamaPasien().toLowerCase().contains(searchText);
                })
                .filter(antrian -> {
                    String selectedPoli = poliFilterComboBox.getValue();
                    if (selectedPoli == null || selectedPoli.equals("Semua Poli")) return true;
                    return antrian.getPoli().equals(selectedPoli);
                })
                .filter(antrian -> {
                    String selectedStatus = statusFilterComboBox.getValue();
                    if (selectedStatus == null || selectedStatus.equals("Semua Status")) return true;
                    return antrian.getStatus().equals(selectedStatus);
                })
                .filter(antrian -> {
                    String selectedDate = dateFilterComboBox.getValue();
                    if (selectedDate == null || selectedDate.equals("Semua Waktu")) return true;

                    LocalDate antrianDate = antrian.getTimestamp().toLocalDate();
                    LocalDate now = LocalDate.now();

                    return switch (selectedDate) {
                        case "Minggu Ini" -> antrianDate.get(WeekFields.ISO.weekOfYear()) == now.get(WeekFields.ISO.weekOfYear()) && antrianDate.getYear() == now.getYear();
                        case "Bulan Ini" -> antrianDate.getMonth() == now.getMonth() && antrianDate.getYear() == now.getYear();
                        case "Tahun Ini" -> antrianDate.getYear() == now.getYear();
                        default -> true;
                    };
                })
                .collect(Collectors.toList());

        antrianListView.setItems(FXCollections.observableArrayList(filteredList));
        if (selected != null && antrianListView.getItems().contains(selected)) {
            antrianListView.getSelectionModel().select(selected);
        }
    }
    
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