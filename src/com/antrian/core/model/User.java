package com.antrian.core.model;

/**
 * Kelas {@code User} merupakan kelas dasar (superclass) untuk semua jenis pengguna
 * dalam sistem antrian pasien, seperti {@link Admin} dan {@link Pasien}.
 * 
 * <p>Kelas ini menyimpan informasi umum pengguna seperti NIK, nama, alamat,
 * nomor telepon, email, tanggal lahir, dan password.</p>
 *
 * <p>Contoh penggunaan:</p>
 * <pre>{@code
 * User user = new User("12345", "Fajar", "Surabaya", "08123456789",
 *     "fajar@example.com", "10/02/2003", "123456");
 * System.out.println(user.getNama());
 * }</pre>
 * 
 * @author
 *  Sulistyo Fajar Pratama,
 *  Dinda Diyah Arifa,
 *  Musthofa Agung Distyawan
 * @version 1.0
 * @since 2025
 */
public class User {

    /** Nomor Induk Kependudukan (NIK) pengguna. */
    protected String nik;
    /** Nama lengkap pengguna. */
    protected String nama;
    /** Alamat tempat tinggal pengguna. */
    protected String alamat;
    /** Nomor telepon pengguna. */
    protected String noTelepon;
    /** Alamat email pengguna. */
    protected String email;
    /** Tanggal lahir pengguna. */
    protected String tanggalLahir;
    /** Kata sandi akun pengguna. */
    protected String password;

    /**
     * Membuat objek {@code User} baru dengan data pengguna umum.
     *
     * @param nik NIK pengguna
     * @param nama Nama lengkap pengguna
     * @param alamat Alamat tempat tinggal pengguna
     * @param noTelepon Nomor telepon pengguna
     * @param email Alamat email pengguna
     * @param tanggalLahir Tanggal lahir pengguna
     * @param password Kata sandi akun pengguna
     */
    public User(String nik, String nama, String alamat, String noTelepon,
                String email, String tanggalLahir, String password) {
        this.nik = nik;
        this.nama = nama;
        this.alamat = alamat;
        this.noTelepon = noTelepon;
        this.email = email;
        this.tanggalLahir = tanggalLahir;
        this.password = password;
    }

    /** @return NIK pengguna */
    public String getNik() { return nik; }

    /** @return Nama lengkap pengguna */
    public String getNama() { return nama; }

    /** @return Alamat pengguna */
    public String getAlamat() { return alamat; }

    /** @return Nomor telepon pengguna */
    public String getNoTelepon() { return noTelepon; }

    /** @return Alamat email pengguna */
    public String getEmail() { return email; }

    /** @return Tanggal lahir pengguna */
    public String getTanggalLahir() { return tanggalLahir; }

    /** @return Kata sandi pengguna */
    public String getPassword() { return password; }

    /**
     * Menghasilkan representasi string dari objek user untuk penyimpanan data.
     *
     * @return Representasi string pengguna dalam format penyimpanan
     */
    @Override
    public String toString() {
        return String.join(";", nik, nama, alamat, noTelepon, email, tanggalLahir, password);
    }
}
