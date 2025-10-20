package com.antrian.core.model;

public class User {
    private String nik;
    private String nama;
    private String alamat;
    private String noTelepon;
    private String email;
    private String tanggalLahir;
    private String password;
    private String role;

    public User(String nik, String nama, String alamat, String noTelepon, String email, String tanggalLahir, String password, String role) {
        this.nik = nik;
        this.nama = nama;
        this.alamat = alamat;
        this.noTelepon = noTelepon;
        this.email = email;
        this.tanggalLahir = tanggalLahir;
        this.password = password;
        this.role = role;
    }

    public String getNik() { return nik; }
    public String getNama() { return nama; }
    public String getAlamat() { return alamat; }
    public String getNoTelepon() { return noTelepon; }
    public String getEmail() { return email; }
    public String getTanggalLahir() { return tanggalLahir; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    @Override
    public String toString() {
        return String.join(";", nik, nama, alamat, noTelepon, email, tanggalLahir, password, role);
    }
}