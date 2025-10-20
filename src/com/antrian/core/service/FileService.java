package com.antrian.core.service;

import com.antrian.core.model.Antrian;
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

public class FileService {
    private static final String USER_FILE = "users.txt";
    private static final String ANTRIAN_FILE = "antrian.txt";

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

    public static void clearAntrianFile() throws IOException {
        new FileWriter(ANTRIAN_FILE, false).close();
    }

    public static List<User> loadUsers() throws IOException {
        if (!Files.exists(Paths.get(USER_FILE))) return new ArrayList<>();
        try (var lines = Files.lines(Paths.get(USER_FILE))) {
            return lines.map(FileService::parseUser).filter(Objects::nonNull).collect(Collectors.toList());
        }
    }

    public static void saveUsers(List<User> users) throws IOException {
        List<String> lines = users.stream().map(User::toString).collect(Collectors.toList());
        writeLines(USER_FILE, lines);
    }
    
    public static List<Antrian> loadAntrian() throws IOException {
        if (!Files.exists(Paths.get(ANTRIAN_FILE))) return new ArrayList<>();
        try (var lines = Files.lines(Paths.get(ANTRIAN_FILE))) {
            return lines
                    .map(FileService::parseAntrian)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
    }

    public static void saveAntrian(List<Antrian> antrianList) throws IOException {
        List<String> lines = antrianList.stream().map(Antrian::toString).collect(Collectors.toList());
        writeLines(ANTRIAN_FILE, lines);
    }
    
    public static int getNextAntrianId() throws IOException {
        List<Antrian> antrianList = loadAntrian();
        if (antrianList.isEmpty()) return 1;
        return antrianList.stream().mapToInt(Antrian::getId).max().orElse(0) + 1;
    }

    static void writeLines(String fileName, List<String> lines) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (String line : lines) {
                writer.println(line);
            }
        }
    }

    private static User parseUser(String line) {
        try {
            String[] parts = line.split(";", -1);
            if (parts.length < 8) return null;
            return new User(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7]);
        } catch (Exception e) {
            System.err.println("Gagal mem-parsing baris di users.txt: " + line);
            return null;
        }
    }
    
    private static Antrian parseAntrian(String line) {
        try {
            String[] parts = line.split(";", -1);

            if (parts.length == 10) {
                return new Antrian(
                        Integer.parseInt(parts[0]), parts[1], parts[2], parts[3], parts[4], parts[5], parts[6],
                        parts[7].replace("\\n", "\n"), parts[8], LocalDateTime.parse(parts[9], DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            
            } else if (parts.length == 9) {
                System.out.println("Mendeteksi & memigrasi format data antrian lama untuk Antrian #" + parts[0]);
                String nikPasienLama = parts[1];
                return new Antrian(
                        Integer.parseInt(parts[0]), nikPasienLama, nikPasienLama, parts[2], parts[3], parts[4], parts[5],
                        parts[6].replace("\\n", "\n"), parts[7], LocalDateTime.parse(parts[8], DateTimeFormatter.ISO_LOCAL_DATE_TIME));

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