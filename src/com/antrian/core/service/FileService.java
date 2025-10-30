package com.antrian.core.service;

import com.antrian.core.model.Admin;
import com.antrian.core.model.Pasien;
import com.antrian.core.model.User;
import com.antrian.core.model.Antrian;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Kelas {@code FileService} bertanggung jawab untuk membaca dan menulis
 * data pengguna (User, Admin, Pasien) dan data antrian dari/ke file teks.
 *
 * (Javadoc lainnya tetap sama)
 *
 * @version 1.1 (Modifikasi: Perbaikan NullPointerException pada mkdirs)
 * @since 2025
 */
public class FileService {

    private static final String USER_FILE = "users.txt";
    private static final String ANTRIAN_FILE = "antrian.txt";

    // ===========================
    // ==== USER HANDLER ========
    // ===========================

    /**
     * Membaca seluruh data pengguna dari file User.txt.
     * Data akan dikonversi menjadi objek Admin atau Pasien sesuai dengan role.
     *
     * @return List dari objek User (Admin/Pasien)
     * @throws IOException jika file tidak ditemukan atau rusak
     */
    public static List<User> loadUsers() throws IOException {
        List<User> users = new ArrayList<>();

        File file = new File(USER_FILE);
        if (!file.exists()) {
            System.out.println("[INFO] File User.txt belum ada, membuat file baru...");
            
            // === PERBAIKAN DI SINI ===
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            // =========================
            
            file.createNewFile();
            return users;
        }

        List<String> lines = Files.readAllLines(Paths.get(USER_FILE));

        for (String line : lines) {
            if (line.trim().isEmpty()) continue;

            String[] parts = line.split(";");
            if (parts.length < 8) continue; // format rusak

            String role = parts[parts.length - 1].trim().toLowerCase();

            if (role.equals("role=admin")) {
                // Format: nik;nama;alamat;noTelp;email;lahir;pass;jabatan;role=admin
                users.add(new Admin(
                        parts[0],
                        parts[1],
                        parts[2],
                        parts[3],
                        parts[4],
                        parts[5],
                        parts[6],
                        parts[7]
                ));
            } else if (role.equals("role=pasien")) {
                // Format: nik;nama;alamat;noTelp;email;lahir;pass;rekamMedis;role=pasien
                users.add(new Pasien(
                        parts[0],
                        parts[1],
                        parts[2],
                        parts[3],
                        parts[4],
                        parts[5],
                        parts[6],
                        parts[7]
                ));
            } else {
                // fallback (jaga-jaga)
                users.add(new User(
                        parts[0],
                        parts[1],
                        parts[2],
                        parts[3],
                        parts[4],
                        parts[5],
                        parts[6]
                ));
            }
        }

        return users;
    }

    /**
     * Menyimpan daftar pengguna ke file User.txt.
     * Setiap objek akan dikonversi ke format string sesuai role-nya.
     *
     * @param users daftar user yang akan disimpan
     * @throws IOException jika terjadi kesalahan saat menulis file
     */
    public static void saveUsers(List<User> users) throws IOException {
        File file = new File(USER_FILE);
        
        // === PERBAIKAN DI SINI (Ini adalah error Anda: line 122) ===
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        // =========================================================

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (User user : users) {
                writer.write(user.toString());
                writer.newLine();
            }
        }
    }

    // ===========================
    // ==== ANTRIAN HANDLER ======
    // ===========================

    /**
     * Membaca semua data antrian dari file Antrian.txt.
     *
     * @return List berisi semua data antrian
     * @throws IOException jika file tidak ditemukan atau rusak
     */
    public static List<Antrian> loadAntrian() throws IOException {
        List<Antrian> antrianList = new ArrayList<>();
        File file = new File(ANTRIAN_FILE);

        if (!file.exists()) {
            File parentDir = file.getParentFile(); // Kode ini sudah benar
            if (parentDir != null && !parentDir.exists()) parentDir.mkdirs();
            file.createNewFile();
            return antrianList;
        }

        List<String> lines = Files.readAllLines(Paths.get(ANTRIAN_FILE));
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(";");
            if (parts.length < 10) continue;

            try {
                Antrian a = new Antrian(
                        Integer.parseInt(parts[0]),
                        parts[1],
                        parts[2],
                        parts[3],
                        parts[4],
                        parts[5],
                        parts[6],
                        parts[7].replace("\\n", "\n"),
                        parts[8],
                        LocalDateTime.parse(parts[9], formatter)
                );
                antrianList.add(a);
            } catch (Exception e) {
                System.err.println("[WARN] Gagal parse antrian: " + line);
            }
        }

        return antrianList;
    }

    /**
     * Menyimpan daftar antrian ke file Antrian.txt.
     *
     * @param antrianList daftar antrian yang akan disimpan
     * @throws IOException jika terjadi kesalahan saat menulis file
     */
    public static void saveAntrian(List<Antrian> antrianList) throws IOException {
        File file = new File(ANTRIAN_FILE);
        File parentDir = file.getParentFile(); // Kode ini sudah benar
        if (parentDir != null && !parentDir.exists()) parentDir.mkdirs();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Antrian a : antrianList) {
                writer.write(a.toString());
                writer.newLine();
            }
        }
    }

    // ===========================
    // ==== UTILITIES ============
    // ===========================

    /**
     * Menambahkan satu user baru ke file tanpa menimpa data sebelumnya.
     *
     * @param user user baru
     * @throws IOException jika gagal menulis ke file
     */
    public static void appendUser(User user) throws IOException {
        File file = new File(USER_FILE);
        
        // === PERBAIKAN DI SINI ===
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        // =========================

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(user.toString());
            writer.newLine();
        }
    }

    /**
     * Menambahkan satu data antrian ke file tanpa menimpa data sebelumnya.
     *
     * @param antrian data antrian baru
     * @throws IOException jika gagal menulis ke file
     */
    public static void appendAntrian(Antrian antrian) throws IOException {
        File file = new File(ANTRIAN_FILE);
        File parentDir = file.getParentFile(); // Kode ini sudah benar
        if (parentDir != null && !parentDir.exists()) parentDir.mkdirs();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(antrian.toString());
            writer.newLine();
        }
    }

    /**
     * Memuat semua antrian dan memperbarui status otomatis jika diperlukan.
     * Untuk kompatibilitas dengan controller lama.
     *
     * @return Daftar antrian yang telah diproses
     * @throws IOException jika gagal membaca file
     */
    public static List<Antrian> loadAndProcessAntrianStatus() throws IOException {
        List<Antrian> list = loadAntrian();

        // Jika ingin memproses otomatis, misal ubah status "Baru" menjadi "Diproses"
        for (Antrian a : list) {
            if (a.getStatus() == null || a.getStatus().isEmpty()) {
                a.setStatus("Baru");
            }
        }

        return list;
    }

    /**
     * Mengambil ID antrian berikutnya berdasarkan data terakhir di file.
     *
     * @return nilai ID berikutnya (increment)
     * @throws IOException jika file tidak dapat dibaca
     */
    public static int getNextAntrianId() throws IOException {
        List<Antrian> list = loadAntrian();
        if (list.isEmpty()) {
            return 1;
        } else {
            return list.get(list.size() - 1).getId() + 1;
        }
    }

    /**
     * Menghapus seluruh data dalam file Antrian.txt.
     *
     * @throws IOException jika gagal membersihkan file
     */
    public static void clearAntrianFile() throws IOException {
        File file = new File(ANTRIAN_FILE);
        if (file.exists()) {
            new FileWriter(file, false).close();
            System.out.println("[INFO] Semua data antrian telah dihapus.");
        }
    }

}