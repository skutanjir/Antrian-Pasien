package com.antrian.ui;

import com.antrian.core.model.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Kelas utama {@code Main} merupakan titik masuk (entry point)
 * dari aplikasi Antrian Pasien berbasis JavaFX.
 * <p>
 * Kelas ini bertanggung jawab untuk memuat tampilan awal (Login.fxml),
 * menyimpan informasi pengguna yang sedang login, serta menyediakan
 * fungsi utilitas untuk mengganti scene antar halaman.
 * </p>
 *
 * <p>Fitur utama:</p>
 * <ul>
 *   <li>Menangani inisialisasi JavaFX Stage utama.</li>
 *   <li>Menyimpan data sesi pengguna yang sedang login.</li>
 *   <li>Mengatur transisi antar tampilan dengan metode {@link #changeScene(String, double, double)}.</li>
 * </ul>
 *
 * @author
 *  Sulistyo Fajar Pratama,
 *  Dinda Diyah Arifa,
 *  Musthofa Agung Distyawan
 * @version 1.0
 * @since 2025
 */
public class Main extends Application {

    /** Stage utama aplikasi yang digunakan untuk mengganti scene. */
    private static Stage stg;

    /** Menyimpan pengguna yang sedang login (Admin atau Pasien). */
    public static User loggedInUser;

    /**
     * Memulai aplikasi JavaFX dan memuat tampilan awal (halaman login).
     *
     * @param primaryStage stage utama JavaFX
     * @throws Exception jika file FXML tidak ditemukan
     */
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

    /**
     * Mengganti scene saat ini dengan tampilan baru berdasarkan file FXML yang diberikan.
     *
     * @param fxml path file FXML (misal: "/fxml/AdminDashboard.fxml")
     * @param width lebar jendela baru
     * @param height tinggi jendela baru
     * @throws IOException jika file FXML tidak dapat dimuat
     */
    public void changeScene(String fxml, double width, double height) throws IOException {
        Parent pane = FXMLLoader.load(getClass().getResource(fxml));
        stg.getScene().setRoot(pane);
    }

    /**
     * Mengambil objek {@link Stage} utama aplikasi.
     *
     * @return stage utama aplikasi
     */
    public static Stage getStage() {
        return stg;
    }

    /**
     * Metode utama untuk menjalankan aplikasi JavaFX.
     *
     * @param args argumen yang diteruskan ke aplikasi
     */
    public static void main(String[] args) {
        launch(args);
    }
}
