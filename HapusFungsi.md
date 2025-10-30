# 📖 Alur Kerja Fungsi "Hapus Antrian"

Dokumen ini menjelaskan alur data dan file/kelas yang terlibat saat seorang Admin menghapus antrian di Dashboard.

---

### 1. 🖱️ Pemicu (Admin Memilih Antrian)

Langkah ini sama persis dengan "Update". Admin sudah memilih item, dan panel detail di sebelah kanan sudah terisi.

* **File/Kelas:** `AdminDashboardController.java`
* **Status:** Panel detail sedang menampilkan data antrian yang akan dihapus.

---

### 2. 🗑️ Aksi (Admin Menekan Tombol Hapus)

Admin menekan tombol "Hapus" di panel detail.

* **Tampilan (Wajah):**
    `AdminDashboard.fxml`
* **Komponen:**
    Tombol "Hapus" (dengan `fx:id="hapusButton"`)
* **Logika (Otak):**
    `AdminDashboardController.java`
* **Fungsi yang Terlibat:**
    * `handleHapus()`: Ini adalah fungsi utama yang dieksekusi.
    * **Langkah Tambahan:** `handleHapus()` akan memunculkan `Alert` (pop-up) untuk konfirmasi ("Anda yakin?"). Program akan berhenti sejenak menunggu Admin menekan "YES" atau "NO".

---

### 3. 💾 Proses Penghapusan (Jika Admin Menekan "YES")

Jika Admin mengonfirmasi, fungsi `handleHapus()` melanjutkan proses "Baca-Ubah-Timpa".

#### Langkah 3a: BACA

* `handleHapus()` memanggil `FileService.loadAntrian()`.
* `FileService.loadAntrian()` **membaca ulang seluruh file `antrian.txt`** dan mengembalikan `List<Antrian>` (daftar lengkap) ke dalam `handleHapus()`.

#### Langkah 3b: UBAH (HAPUS)

* `handleHapus()` menjalankan fungsi `removeIf()` pada `List` yang baru saja dibaca.
    ```java
    // Di dalam handleHapus()
    antrianList.removeIf(a -> a.getId() == currentAntrian.getId());
    ```
* Logika ini mencari antrian yang ID-nya cocok, lalu **menghapusnya dari `List`**.
* **Hasil:** `antrianList` sekarang berisi semua antrian *kecuali* antrian yang baru saja dihapus.
* Saat ini, file `antrian.txt` di hard drive **masih belum berubah**.

#### Langkah 3c: TIMPA (Simpan)

* `handleHapus()` memanggil `FileService.saveAntrian()` dan mengirimkan `List` yang sudah *lebih pendek* tadi.
* `FileService.saveAntrian()` **MENGHAPUS** seluruh isi file `antrian.txt` yang lama.
* Fungsi ini lalu **MENULIS ULANG** `List` yang sudah (berkurang satu) tadi baris per baris ke dalam file `antrian.txt` yang sudah kosong.
* **Sekarang, file `antrian.txt` di hard drive Anda sudah ter-update (datanya berkurang satu).**

---

### 4. 🔄 Refresh Tampilan (Selesai Menghapus)

Setelah Langkah 3c selesai, `handleHapus` melakukan satu hal terakhir.

* **File/Kelas:** `AdminDashboardController.java`
* **Fungsi:** `handleHapus()` memanggil `loadAntrianData()`
* **Alur:**
    1.  `handleHapus()` memanggil `loadAntrianData()`.
    2.  `loadAntrianData()` membaca file `antrian.txt` (yang datanya sudah berkurang).
    3.  Daftar antrian yang *baru* ini ditampilkan di `antrianListView`.
    4.  Anda melihat item yang dihapus menghilang dari daftar di sebelah kiri.
    5.  Panel detail di sebelah kanan otomatis **disembunyikan** (`detailPane.setVisible(false)`).
