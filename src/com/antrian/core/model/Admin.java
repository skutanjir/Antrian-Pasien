package com.antrian.core.model;

/**
 * Kelas {@code Admin} merepresentasikan pengguna dengan hak akses administratif
 * dalam sistem antrian pasien. Kelas ini merupakan subclass dari {@link User}
 * dan menambahkan atribut {@code jabatan} untuk menjelaskan posisi admin.
 *
 * <p>Contoh penggunaan:</p>
 * <pre>{@code
 * Admin admin = new Admin("123", "Fajar", "Surabaya", "08123456789",
 *         "admin@gmail.com", "10/02/2003", "admin123", "Administrator");
 * System.out.println(admin.getNama());
 * }</pre>
 *
 * @author
 *  Sulistyo Fajar Pratama
 *  Dinda Diyah Arifa
 *  Musthofa Agung Distyawan
 * @version 1.0
 * @since 2025
 */
public class Admin extends User {

    /** Jabatan atau posisi admin dalam sistem. */
    private String jabatan;

    /**
     * Membuat objek {@code Admin} baru dengan data pengguna dan jabatan tertentu.
     *
     * @param nik NIK admin
     * @param nama Nama lengkap admin
     * @param alamat Alamat tempat tinggal admin
     * @param noTelepon Nomor telepon admin
     * @param email Alamat email admin
     * @param tanggalLahir Tanggal lahir admin
     * @param password Kata sandi akun admin
     * @param jabatan Jabatan atau posisi admin dalam sistem
     */
    public Admin(String nik, String nama, String alamat, String noTelepon,
                 String email, String tanggalLahir, String password, String jabatan) {
        super(nik, nama, alamat, noTelepon, email, tanggalLahir, password);
        this.jabatan = jabatan;
    }

    /**
     * Mengambil jabatan admin.
     *
     * @return Jabatan admin
     */
    public String getJabatan() {
        return jabatan;
    }

    /**
     * Menghasilkan representasi string dari objek admin.
     * Formatnya mencakup seluruh data pengguna ditambah jabatan dan role.
     *
     * @return Representasi string admin dalam format penyimpanan data
     */
    @Override
    public String toString() {
        return super.toString() + ";" + jabatan + ";role=admin";
    }
}
