package com.antrian.core.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Antrian {
    private int id;
    private String nikPembuat;
    private String nikPasien;
    private String namaPasien;
    private String alamatPasien;
    private String noTeleponPasien;
    private String poli;
    private String keluhan;
    private String status;
    private LocalDateTime timestamp;

    public Antrian(int id, String nikPembuat, String nikPasien, String namaPasien, String alamatPasien, String noTeleponPasien, String poli, String keluhan, String status, LocalDateTime timestamp) {
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

    public int getId() { return id; }
    public String getNikPembuat() { return nikPembuat; }
    public String getNikPasien() { return nikPasien; }
    public String getNamaPasien() { return namaPasien; }
    public String getAlamatPasien() { return alamatPasien; }
    public String getNoTeleponPasien() { return noTeleponPasien; }
    public String getPoli() { return poli; }
    public String getKeluhan() { return keluhan; }
    public String getStatus() { return status; }
    public LocalDateTime getTimestamp() { return timestamp; }
    
    public void setStatus(String status) { 
        this.status = status;
    }

    public String getFormattedTimestamp() {
        return timestamp.format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy"));
    }

    @Override
    public String toString() {
        return String.join(";", String.valueOf(id), nikPembuat, nikPasien, namaPasien, alamatPasien, noTeleponPasien, poli, keluhan.replace("\n", "\\n"), status, timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Antrian antrian = (Antrian) o;
        return id == antrian.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}