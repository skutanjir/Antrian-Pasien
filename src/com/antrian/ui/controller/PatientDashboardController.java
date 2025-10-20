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

public class PatientDashboardController extends BaseDashboardController {

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