# 📖 Alur Lengkap: Dari Load Awal Hingga Update Status

Dokumen ini menjelaskan alur kerja lengkap di dalam kode, dimulai dari data dimuat ke layar dashboard Admin, hingga Admin menyimpan perubahan status antrian.

---

### 1. 🖥️ Load Awal (Saat Dashboard Pertama Dibuka)

Ini adalah bagaimana daftar antrian muncul di layar Anda.

* **File/Kelas:** `AdminDashboardController.java`
* **Fungsi:** `initialize()` (Ini berjalan otomatis saat FXML dimuat)
* **Alur:**
    1.  `initialize()` memanggil fungsi `loadAntrianData()`.
    2.  `loadAntrianData()` memanggil `FileService.loadAntrian()`.
    3.  `FileService.loadAntrian()` membuka file **`antrian.txt`**, membaca semua baris, dan mengembalikannya sebagai `List<Antrian>`.
    4.  `loadAntrianData()` menerima `List` tersebut dan menampilkannya di `antrianListView` (daftar di sebelah kiri).
    5.  Panel detail (di sebelah kanan) **disembunyikan** (`detailPane.setVisible(false)`).

---

### 2. 🖱️ Aksi Admin (Memilih Item dan Mengubah Data)

* **File/Kelas:** `AdminDashboardController.java`
* **Fungsi:** *Listener* di dalam `initialize()` dan `populateFields()`
* **Alur:**
    1.  Anda mengklik item di `antrianListView`.
    2.  *Listener* yang dipasang di `initialize()` mendeteksi klik ini.
    3.  *Listener* memanggil `populateFields()` dan mengirimkan data antrian yang Anda klik.
    4.  `populateFields()` mengisi semua `TextField` (NIK, Nama, dll.) dan `statusComboBoxDetail` di panel kanan.
    5.  Panel detail **ditampilkan** (`detailPane.setVisible(true)`).
    6.  Anda mengubah nilai `statusComboBoxDetail` (misal, dari "Baru" menjadi "Sedang Berlangsung").

---

### 3. 💾 Proses Simpan (Saat Tombol "Simpan" Ditekan)

Ini adalah inti dari proses "Update". Ini terjadi di dalam satu fungsi.

* **File/Kelas:** `AdminDashboardController.java`
* **Fungsi:** `handleSimpan()`

Fungsi `handleSimpan()` melakukan 3 hal secara berurutan: **BACA**, **UBAH**, dan **TIMPA**.

#### Langkah 3a: BACA

* `handleSimpan()` memanggil `FileService.loadAntrian()`.
* `FileService.loadAntrian()` **membaca ulang seluruh file `antrian.txt`** dan mengembalikan `List<Antrian>` (daftar antrian) yang masih *fresh* dari hard drive.

#### Langkah 3b: UBAH

* `handleSimpan()` mencari antrian yang Anda pilih di dalam `List` yang baru saja dibaca tadi.
* Fungsi ini **mengubah status** antrian tersebut *di dalam List* (misal, `Antrian #3` statusnya diubah menjadi "Sedang Berlangsung").
* Saat ini, file `antrian.txt` di hard drive **masih belum berubah**.

#### Langkah 3c: TIMPA (Simpan)

* `handleSimpan()` memanggil `FileService.saveAntrian()` dan mengirimkan `List` yang sudah diubah di Langkah 3b.
* `FileService.saveAntrian()` **MENGHAPUS** seluruh isi file `antrian.txt` yang lama.
* Fungsi ini lalu **MENULIS ULANG** seluruh `List` yang sudah diubah tadi baris per baris ke dalam file `antrian.txt` yang sudah kosong.
* **Sekarang, file `antrian.txt` di hard drive Anda sudah ter-update.**

---

### 4. 🔄 Refresh Tampilan (Selesai Menyimpan)

Setelah Langkah 3c selesai, `handleSimpan` melakukan satu hal terakhir.

* **File/Kelas:** `AdminDashboardController.java`
* **Fungsi:** `handleSimpan()` memanggil `loadAntrianData()`
* **Alur:**
    1.  `handleSimpan()` memanggil `loadAntrianData()` (fungsi yang sama di Langkah 1).
    2.  `loadAntrianData()` memanggil `FileService.loadAntrian()` lagi.
    3.  `FileService.loadAntrian()` membaca file `antrian.txt` (yang **sudah ter-update** dari Langkah 3c).
    4.  Daftar antrian yang *baru* ini ditampilkan di `antrianListView`.
    5.  Anda melihat status di daftar sebelah kiri berubah.
