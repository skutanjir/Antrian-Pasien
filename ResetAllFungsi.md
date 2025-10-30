# 📖 Alur Kerja Fungsi "Reset Data Antrian"

Dokumen ini menjelaskan alur kerja saat Admin menekan tombol "Reset Data" (`handleResetAntrian`).

---

### 1. 🖱️ Aksi (Admin Menekan Tombol Reset)

Admin menekan tombol "Reset Data" di bagian atas dashboard.

* **Tampilan (Wajah):**
    `AdminDashboard.fxml`
* **Komponen:**
    Tombol "Reset Data" (dengan `fx:id="resetButton"`)
* **Logika (Otak):**
    `AdminDashboardController.java`
* **Fungsi yang Terlibat:**
    * `handleResetAntrian()`: Ini adalah fungsi utama yang dieksekusi.
    * **Langkah Tambahan:** `handleResetAntrian()` akan memunculkan `Alert` (pop-up) untuk konfirmasi ("Anda yakin ingin menghapus SEMUA data?").

---

### 2. 💥 Proses Pengosongan (Jika Admin Menekan "YES")

Jika Admin mengonfirmasi, fungsi `handleResetAntrian()` melanjutkan proses. Tidak ada proses "Baca" atau "Ubah".

* **Logika (Otak):Semua:**
    `AdminDashboardController.java` memanggil `FileService.java`
* **File Data (Database):**
    `antrian.txt`
* **Fungsi yang Terlibat:**
    1.  `handleResetAntrian()` (dari `AdminDashboardController`) memanggil `FileService.clearAntrianFile()`.
    2.  Fungsi `clearAntrianFile()` (di `FileService.java`) membuka file `antrian.txt` dalam mode "Tulis" (Write), **bukan** "Tambah" (Append).
    3.  Saat file dibuka dalam mode "Tulis" (`new FileWriter(file, false)`), sistem operasi **langsung mengosongkan (menghapus) semua isi** file tersebut.
    4.  Fungsi tersebut langsung menutup file yang sudah kosong itu.

* **Hasil:** File `antrian.txt` sekarang ada, tetapi isinya kosong (0 byte).

---

### 3. 🔄 Refresh Tampilan (Selesai Menghapus)

Setelah file `antrian.txt` kosong, tampilan harus diperbarui.

* **File/Kelas:**
    `AdminDashboardController.java`
* **Fungsi:**
    `handleResetAntrian()` memanggil `loadAntrianData()`
* **Alur:**
    1.  `handleResetAntrian()` memanggil `loadAntrianData()`.
    2.  `loadAntrianData()` memanggil `FileService.loadAntrian()`.
    3.  `FileService.loadAntrian()` membaca file `antrian.txt` (yang sekarang **kosong**).
    4.  Fungsi ini mengembalikan sebuah `List` (daftar) yang **kosong**.
    5.  `antrianListView` (daftar di sebelah kiri) diisi dengan `List` kosong tersebut.
    6.  **Hasil:** Semua item antrian menghilang dari layar.
