# 📖 Alur Kerja Fungsi "Buat Antrian Baru"

Dokumen ini menjelaskan alur kerja lengkap saat seorang Pasien membuat antrian baru, mulai dari menekan tombol di dashboard hingga antrian tersebut tersimpan.

---

### 1. 🖱️ Pemicu (Pasien Ingin Buat Antrian)

Pasien menekan tombol "Buat Antrian" di `PatientDashboard`.

* **File/Kelas Asal:**
    `PatientDashboardController.java`
* **Fungsi yang Dipanggil:**
    `handleBuatAntrian()`
* **Apa yang Terjadi:**
    1.  Fungsi ini menggunakan `FXMLLoader` untuk memuat file **`BuatAntrian.fxml`** (Tampilan/Wajah).
    2.  Fungsi ini juga memuat **`BuatAntrianController.java`** (Logika/Otak) yang terhubung dengan FXML tersebut.
    3.  Sebuah `Stage` (jendela pop-up/modal) baru dibuat untuk menampilkan form "Buat Antrian".
    4.  **PENTING:** `PatientDashboardController` mengirimkan *dirinya sendiri* (khususnya fungsi `loadAntrianData()`) sebagai **"Callback"** ke `BuatAntrianController`. Ini seperti memberi nomor telepon agar bisa dihubungi kembali nanti.

---

### 2. ✏️ Aksi (Pasien Mengisi Form)

Pasien mengisi semua data di form "Buat Antrian" (NIK, Nama, Poli, Keluhan) lalu menekan tombol "Ambil Antrian".

* **Tampilan (Wajah):**
    `BuatAntrian.fxml`
* **Logika (Otak):**
    `BuatAntrianController.java`
* **Fungsi yang Dipanggil:**
    `handleAmbilAntrian()`

---

### 3. 💾 Proses Penyimpanan (Di Balik Layar)

Fungsi `handleAmbilAntrian()` melakukan proses "Baca-Ubah-Timpa" pada file `.txt`.

* **File/Kelas yang Terlibat:**
    `BuatAntrianController.java` dan `FileService.java`
* **File Data (Database):**
    `antrian.txt`
* **Alur:**
    1.  **Validasi:** Kode memeriksa apakah semua `TextField` sudah diisi. Jika belum, proses berhenti dan menampilkan error.
    2.  **Dapatkan ID Baru:** Memanggil `FileService.getNextAntrianId()` untuk mendapatkan ID unik baru (misal, #5).
    3.  **BACA:** Memanggil `FileService.loadAntrian()` untuk **membaca seluruh isi** `antrian.txt` ke dalam sebuah `List`.
    4.  **UBAH (Tambah):** Membuat objek `Antrian` baru (`newAntrian`) dengan data dari form. Objek baru ini kemudian **ditambahkan** (`.add()`) ke dalam `List` yang tadi dibaca.
    5.  **TIMPA (Simpan):** Memanggil `FileService.saveAntrian()` untuk **menimpa (overwrite)** seluruh file `antrian.txt` dengan `List` yang sudah berisi data baru tersebut.
    6.  **Hasil:** File `antrian.txt` di hard drive Anda sudah ter-update (datanya bertambah satu).

---

### 4. 🔄 Umpan Balik (Refresh Tampilan)

Setelah data berhasil disimpan ke file `.txt`:

* **File/Kelas:**
    `BuatAntrianController.java`
* **Fungsi:**
    `handleAmbilAntrian()`
* **Alur:**
    1.  **Tampilkan Sukses:** Menampilkan `Alert` (pop-up) bahwa antrian berhasil dibuat.
    2.  **Jalankan Callback:** Memanggil `callback.run()`. (Ingat, "callback" ini adalah fungsi `loadAntrianData()` dari `PatientDashboardController` yang dikirim di Langkah 1).
    3.  **Tutup Pop-up:** Memanggil `closeWindow()` untuk menutup jendela "Buat Antrian".
    4.  **Dashboard Refresh:** Di *belakang layar*, `PatientDashboardController.loadAntrianData()` berjalan, memuat ulang data dari `antrian.txt`, dan menampilkan antrian baru (#5) di daftar dashboard pasien.
