package com.antrian.ui;

import com.antrian.core.model.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Stage stg;
    public static User loggedInUser;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stg = primaryStage;
        
        primaryStage.setResizable(true);
        
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        primaryStage.setTitle("Aplikasi Antrian");

        try {
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/img/logo.png")));
        } catch (Exception e) {
            System.err.println("Gagal memuat ikon aplikasi: " + e.getMessage());
        }

        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.show();
        
        primaryStage.setMaximized(true);
    }

    public void changeScene(String fxml, double width, double height) throws IOException {
        Parent pane = FXMLLoader.load(getClass().getResource(fxml));
        stg.getScene().setRoot(pane);
    }
    
    public static Stage getStage() {
        return stg;
    }

    public static void main(String[] args) {
        launch(args);
    }
}