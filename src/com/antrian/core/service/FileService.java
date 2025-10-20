package com.antrian.core.service;

import com.antrian.core.model.Admin;
import com.antrian.core.model.Antrian;
import com.antrian.core.model.Pasien;
import com.antrian.core.model.User;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Kelas {@code FileService} bertanggung jawab untuk menangani proses input-output file
 * pada sistem antrian pasien. Termasuk penyimpanan dan pembacaan data pengguna serta data antrian.
 *
 * <p>Kelas ini menyediakan fungsi untuk:</p>
 * <ul>
 *   <li>Membaca dan menyimpan data {@link User}, {@link Admin}, dan {@link Pasien} dari <b>users.txt</b>.</li>
 *   <li>Membaca dan menyimpan data {@link Antrian} dari <b>antrian.txt</b>.</li>
 *   <li>Memproses status antrian secara otomatis berdasarkan waktu.</li>
 * </ul>
 *
 * <p>Semua data disimpan dalam format teks dengan delimiter semikolon (";").</p>
 *
 * @author
 *  Sulistyo Fajar Pratama,
 *  Dinda Diyah Arifa,
 *  Musthofa Agung Distyawan
 * @version 1.0
 * @since 2025
 */
public class FileService {
    /** Nama file penyimpanan data pengguna. */
    private static final String USER_FILE = "users.txt";
    /** Nama file penyimpanan data antrian. */
    private static final String ANTRIAN_FILE = "antrian.txt";

    /**
     * Memuat data antrian dari file dan memperbarui status antrian secara otomatis.
     * Jika antrian berstatus "Baru" lebih dari 60 detik, status akan berubah menjadi "Sedang Berlangsung".
     *
     * @return Daftar objek {@link Antrian} yang telah diproses
     * @throws IOException jika gagal membaca atau menulis file
     */
    public static List<Antrian> loadAndProcessAntrianStatus() throws IOException {
        List<Antrian> antrianList = loadAntrian();
        boolean needsSaving = false;
        LocalDateTime now = LocalDateTime.now();

        for (Antrian antrian : antrianList) {
            if ("Baru".equals(antrian.getStatus())) {
                Duration duration = Duration.between(antrian.getTimestamp(), now);
                if (duration.toSeconds() >= 60) {
                    antrian.setStatus("Sedang Berlangsung");
                    needsSaving = true;
                }
            }
        }

        if (needsSaving) {
            saveAntrian(antrianList);
        }

        return antrianList;
    }

    /**
     * Menghapus seluruh isi file antrian.
     * @throws IOException jika terjadi kesalahan saat mengosongkan file
     */
    public static void clearAntrianFile() throws IOException {
        new FileWriter(ANTRIAN_FILE, false).close();
    }

    /**
     * Memuat seluruh data pengguna dari file {@code users.txt}.
     * @return Daftar pengguna yang berhasil dimuat
     * @throws IOException jika file tidak ditemukan atau gagal dibaca
     */
    public static List<User> loadUsers() throws IOException {
        if (!Files.exists(Paths.get(USER_FILE))) return new ArrayList<>();
        try (var lines = Files.lines(Paths.get(USER_FILE))) {
            return lines.map(FileService::parseUser).filter(Objects::nonNull).collect(Collectors.toList());
        }
    }

    /**
     * Menyimpan daftar pengguna ke file {@code users.txt}.
     * @param users daftar pengguna yang akan disimpan
     * @throws IOException jika gagal menulis ke file
     */
    public static void saveUsers(List<User> users) throws IOException {
        List<String> lines = users.stream().map(User::toString).collect(Collectors.toList());
        writeLines(USER_FILE, lines);
    }

    /**
     * Memuat seluruh data antrian dari file {@code antrian.txt}.
     * @return Daftar antrian yang berhasil dimuat
     * @throws IOException jika file tidak ditemukan atau gagal dibaca
     */
    public static List<Antrian> loadAntrian() throws IOException {
        if (!Files.exists(Paths.get(ANTRIAN_FILE))) return new ArrayList<>();
        try (var lines = Files.lines(Paths.get(ANTRIAN_FILE))) {
            return lines.map(FileService::parseAntrian).filter(Objects::nonNull).collect(Collectors.toList());
        }
    }

    /**
     * Menyimpan daftar antrian ke file {@code antrian.txt}.
     * @param antrianList daftar antrian yang akan disimpan
     * @throws IOException jika gagal menulis ke file
     */
    public static void saveAntrian(List<Antrian> antrianList) throws IOException {
        List<String> lines = antrianList.stream().map(Antrian::toString).collect(Collectors.toList());
        writeLines(ANTRIAN_FILE, lines);
    }

    /**
     * Mengambil ID antrian berikutnya secara otomatis.
     * @return ID baru (nilai tertinggi + 1)
     * @throws IOException jika gagal membaca file
     */
    public static int getNextAntrianId() throws IOException {
        List<Antrian> antrianList = loadAntrian();
        if (antrianList.isEmpty()) return 1;
        return antrianList.stream().mapToInt(Antrian::getId).max().orElse(0) + 1;
    }

    /**
     * Menulis daftar string ke dalam file teks.
     * @param fileName nama file tujuan
     * @param lines daftar baris yang akan ditulis
     * @throws IOException jika gagal menulis file
     */
    static void writeLines(String fileName, List<String> lines) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (String line : lines) {
                writer.println(line);
            }
        }
    }

    /**
     * Mengubah satu baris teks dari file {@code users.txt} menjadi objek {@link User}.
     * Jika terdapat penanda role, akan dikonversi ke subclass {@link Admin} atau {@link Pasien}.
     *
     * @param line Baris teks dari file
     * @return Objek {@code User}, {@code Admin}, atau {@code Pasien}
     */
    private static User parseUser(String line) {
        try {
            String[] parts = line.split(";", -1);
            if (parts.length < 7) return null;

            String role = parts.length >= 8 ? parts[7] : "";

            if ("admin".equalsIgnoreCase(role)) {
                return new Admin(parts[0], parts[1], parts[2], parts[3],
                        parts[4], parts[5], parts[6], "Administrator");
            } else {
                return new Pasien(parts[0], parts[1], parts[2], parts[3],
                        parts[4], parts[5], parts[6], "RM-" + parts[0]);
            }

        } catch (Exception e) {
            System.err.println("Gagal mem-parsing baris di users.txt: " + line);
            return null;
        }
    }

    /**
     * Mengubah satu baris teks dari file {@code antrian.txt} menjadi objek {@link Antrian}.
     * Mendukung dua format: format baru (10 kolom) dan format lama (9 kolom).
     *
     * @param line Baris teks dari file
     * @return Objek {@link Antrian} yang berhasil dibuat, atau null jika data korup
     */
    private static Antrian parseAntrian(String line) {
        try {
            String[] parts = line.split(";", -1);

            if (parts.length == 10) {
                return new Antrian(
                        Integer.parseInt(parts[0]), parts[1], parts[2], parts[3], parts[4], parts[5], parts[6],
                        parts[7].replace("\\n", "\n"), parts[8],
                        LocalDateTime.parse(parts[9], DateTimeFormatter.ISO_LOCAL_DATE_TIME));

            } else if (parts.length == 9) {
                System.out.println("Mendeteksi & memigrasi format data antrian lama untuk Antrian #" + parts[0]);
                String nikPasienLama = parts[1];
                return new Antrian(
                        Integer.parseInt(parts[0]), nikPasienLama, nikPasienLama, parts[2], parts[3], parts[4], parts[5],
                        parts[6].replace("\\n", "\n"), parts[7],
                        LocalDateTime.parse(parts[8], DateTimeFormatter.ISO_LOCAL_DATE_TIME));

            } else {
                System.err.println("Mengabaikan baris data korup di antrian.txt: " + line);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Gagal mem-parsing baris di antrian.txt: " + line);
            return null;
        }
    }
}
