package com.antrian.core.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Kelas {@code Antrian} merepresentasikan entitas antrian pasien dalam sistem.
 * Menyimpan informasi terkait pasien, poli, keluhan, status, dan waktu pembuatan.
 * 
 * <p>Kelas ini digunakan untuk mencatat dan menampilkan data antrian yang dibuat
 * oleh pasien melalui sistem registrasi.</p>
 *
 * <p>Contoh penggunaan:</p>
 * <pre>{@code
 * Antrian a = new Antrian(1, "12345", "67890", "Fajar",
 *     "Jl. Pahlawan No.1", "08123456789", "Poli Umum",
 *     "Demam dan batuk", "Baru", LocalDateTime.now());
 * System.out.println(a.getFormattedTimestamp());
 * }</pre>
 * 
 * @author
 *  Sulistyo Fajar Pratama
 *  Dinda Diyah Arifa
 *  Musthofa Agung Distyawan
 * @version 1.0
 * @since 2025
 */
public class Antrian {

    /** ID unik untuk setiap antrian. */
    private int id;
    /** NIK pembuat antrian (biasanya pasien). */
    private String nikPembuat;
    /** NIK pasien yang terdaftar di antrian. */
    private String nikPasien;
    /** Nama lengkap pasien. */
    private String namaPasien;
    /** Alamat tempat tinggal pasien. */
    private String alamatPasien;
    /** Nomor telepon pasien. */
    private String noTeleponPasien;
    /** Poli tujuan pasien (misalnya: Umum, Gigi, Anak, Jantung). */
    private String poli;
    /** Keluhan atau deskripsi kondisi pasien. */
    private String keluhan;
    /** Status antrian (misalnya: Baru, Sedang Berlangsung, Selesai, Batal). */
    private String status;
    /** Waktu pembuatan antrian. */
    private LocalDateTime timestamp;

    /**
     * Membuat objek {@code Antrian} baru dengan informasi lengkap.
     *
     * @param id ID unik antrian
     * @param nikPembuat NIK pembuat antrian
     * @param nikPasien NIK pasien
     * @param namaPasien Nama pasien
     * @param alamatPasien Alamat pasien
     * @param noTeleponPasien Nomor telepon pasien
     * @param poli Poli tujuan pasien
     * @param keluhan Keluhan pasien
     * @param status Status antrian
     * @param timestamp Waktu pembuatan antrian
     */
    public Antrian(int id, String nikPembuat, String nikPasien, String namaPasien,
                   String alamatPasien, String noTeleponPasien, String poli,
                   String keluhan, String status, LocalDateTime timestamp) {
        this.id = id;
        this.nikPembuat = nikPembuat;
        this.nikPasien = nikPasien;
        this.namaPasien = namaPasien;
        this.alamatPasien = alamatPasien;
        this.noTeleponPasien = noTeleponPasien;
        this.poli = poli;
        this.keluhan = keluhan;
        this.status = status;
        this.timestamp = timestamp;
    }

    /** @return ID antrian */
    public int getId() { return id; }

    /** @return NIK pembuat antrian */
    public String getNikPembuat() { return nikPembuat; }

    /** @return NIK pasien */
    public String getNikPasien() { return nikPasien; }

    /** @return Nama pasien */
    public String getNamaPasien() { return namaPasien; }

    /** @return Alamat pasien */
    public String getAlamatPasien() { return alamatPasien; }

    /** @return Nomor telepon pasien */
    public String getNoTeleponPasien() { return noTeleponPasien; }

    /** @return Nama poli yang dituju */
    public String getPoli() { return poli; }

    /** @return Keluhan pasien */
    public String getKeluhan() { return keluhan; }

    /** @return Status antrian saat ini */
    public String getStatus() { return status; }

    /** @return Waktu pembuatan antrian */
    public LocalDateTime getTimestamp() { return timestamp; }

    /**
     * Mengubah status antrian (misalnya dari "Baru" menjadi "Selesai").
     * 
     * @param status Status baru antrian
     */
    public void setStatus(String status) { 
        this.status = status;
    }

    /**
     * Mengembalikan waktu pembuatan antrian dalam format yang mudah dibaca.
     * 
     * @return Waktu dalam format "HH:mm dd-MM-yyyy"
     */
    public String getFormattedTimestamp() {
        return timestamp.format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy"));
    }

    /**
     * Mengubah data antrian menjadi format string untuk penyimpanan ke file.
     * 
     * @return Representasi string data antrian
     */
    @Override
    public String toString() {
        return String.join(";",
                String.valueOf(id),
                nikPembuat,
                nikPasien,
                namaPasien,
                alamatPasien,
                noTeleponPasien,
                poli,
                keluhan.replace("\n", "\\n"),
                status,
                timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    /**
     * Membandingkan dua objek antrian berdasarkan ID-nya.
     * 
     * @param o Objek lain yang akan dibandingkan
     * @return true jika kedua objek memiliki ID yang sama
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Antrian antrian = (Antrian) o;
        return id == antrian.id;
    }

    /**
     * Menghasilkan hash code unik berdasarkan ID antrian.
     * 
     * @return Nilai hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
