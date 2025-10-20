package com.antrian.core.model;

/**
 * Kelas {@code Pasien} merepresentasikan pengguna sistem yang berperan
 * sebagai pasien dalam proses pendaftaran dan antrian pada sistem.
 * 
 * <p>Kelas ini merupakan subclass dari {@link User}, yang menambahkan atribut
 * khusus yaitu {@code noRekamMedis} untuk menyimpan nomor rekam medis pasien.</p>
 *
 * <p>Contoh penggunaan:</p>
 * <pre>{@code
 * Pasien pasien = new Pasien("12345", "Fajar", "Surabaya", "08123456789",
 *     "fajar@example.com", "10/02/2003", "123456", "RM-12345");
 * System.out.println(pasien.getNoRekamMedis());
 * }</pre>
 * 
 * @author
 *  Sulistyo Fajar Pratama,
 *  Dinda Diyah Arifa,
 *  Musthofa Agung Distyawan
 * @version 1.0
 * @since 2025
 */
public class Pasien extends User {

    /** Nomor rekam medis unik untuk setiap pasien. */
    private String noRekamMedis;

    /**
     * Membuat objek {@code Pasien} baru dengan informasi lengkap.
     *
     * @param nik NIK pasien
     * @param nama Nama lengkap pasien
     * @param alamat Alamat tempat tinggal pasien
     * @param noTelepon Nomor telepon pasien
     * @param email Alamat email pasien
     * @param tanggalLahir Tanggal lahir pasien
     * @param password Kata sandi akun pasien
     * @param noRekamMedis Nomor rekam medis unik pasien
     */
    public Pasien(String nik, String nama, String alamat, String noTelepon,
                  String email, String tanggalLahir, String password, String noRekamMedis) {
        super(nik, nama, alamat, noTelepon, email, tanggalLahir, password);
        this.noRekamMedis = noRekamMedis;
    }

    /**
     * Mengambil nomor rekam medis pasien.
     *
     * @return Nomor rekam medis pasien
     */
    public String getNoRekamMedis() {
        return noRekamMedis;
    }

    /**
     * Menghasilkan representasi string dari objek pasien.
     * Formatnya mencakup seluruh data pengguna ditambah nomor rekam medis dan role pasien.
     *
     * @return Representasi string pasien dalam format penyimpanan data
     */
    @Override
    public String toString() {
        return super.toString() + ";" + noRekamMedis + ";role=pasien";
    }
}
