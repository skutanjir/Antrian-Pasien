# 📖 Alur Kerja Fungsi "Filter Dashboard"

Dokumen ini menjelaskan alur kerja saat Admin atau Pasien menggunakan `searchField`, `poliFilterComboBox`, atau `statusFilterComboBox`.

---

### 1. 💾 Load Awal (Menyiapkan Data "Master")

Langkah ini terjadi **satu kali** saat dashboard pertama kali dibuka (dijelaskan di `initialize()` -> `loadAntrianData()`).

* **File/Kelas:**
    `BaseDashboardController.java` (dan `Admin/PatientDashboardController`)
* **File Data:**
    `antrian.txt`
* **Alur:**
    1.  `loadAntrianData()` memanggil `FileService.loadAntrian()` untuk membaca **semua** antrian dari `antrian.txt`.
    2.  Semua data ini disimpan dalam sebuah "Master List" di dalam memori (RAM) yang bernama: **`fullAntrianList`**.
    3.  Saat ini, `antrianListView` (tampilan) dan `fullAntrianList` (data memori) isinya sama persis.

---

### 2. ⚙️ Inisialisasi (Pemasangan "Pendengar")

Ini juga terjadi saat dashboard pertama kali dibuka.

* **File/Kelas:**
    `BaseDashboardController.java`
* **Fungsi:**
    `setupFilterControls()` (Dipanggil oleh `initialize()`)
* **Alur:**
    1.  Fungsi ini memasang "Pendengar" (`.addListener()`) pada `searchField`, `poliFilterComboBox`, dan `statusFilterComboBox`.
    2.  "Pendengar" ini diam dan tidak melakukan apa-apa. Tugasnya hanya **menunggu** Anda mengetik atau mengubah pilihan `ComboBox`.

---

### 3. 🖱️ Aksi (Pengguna Mengubah Filter)

Anda mengetik "Budi" di `searchField` ATAU memilih "Poli Gigi" di `ComboBox`.

* **Komponen:**
    `searchField` atau `poliFilterComboBox`
* **Logika (Otak):**
    "Pendengar" yang dipasang di Langkah 2
* **Alur:**
    1.  Saat Anda mengetik satu huruf saja (misal "B"), "Pendengar" di `searchField` langsung aktif.
    2.  "Pendengar" itu segera memanggil satu fungsi: `applyFilters()`.

---

### 4. 📉 Proses Filter (Inti Logika)

Ini adalah bagian paling cerdas. Fungsi ini **TIDAK** membaca ulang file `antrian.txt`.

* **File/Kelas:**
    `BaseDashboardController.java` (atau `AdminDashboardController` yang menimpanya)
* **Fungsi:**
    `applyFilters()`
* **Alur:**
    1.  `applyFilters()` mengambil data dari **`fullAntrianList`** (Master List dari Langkah 1), **bukan** dari file.
    2.  Fungsi ini menggunakan Java Streams (`.stream()`) untuk menyaring `fullAntrianList` secara bertahap:
    3.  **Filter 1 (Nama):** Mengambil nilai dari `searchField` dan menyaring `fullAntrianList`. (Misal: dari 100 antrian, lolos 10).
    4.  **Filter 2 (Poli):** Mengambil nilai dari `poliFilterComboBox` dan menyaring hasil dari Filter 1. (Misal: dari 10 antrian, lolos 3).
    5.  **Filter 3 (Status):** Mengambil nilai dari `statusFilterComboBox` dan menyaring hasil dari Filter 2. (Misal: dari 3 antrian, lolos 2).
    6.  ...dan seterusnya (misal: `dateFilterComboBox` untuk Admin).
    7.  Hasil akhir (2 antrian) dikumpulkan ke dalam `List` baru yang *sementara*.

---

### 5. 🖥️ Refresh Tampilan

* **File/Kelas:**
    `BaseDashboardController.java`
* **Fungsi:**
    `applyFilters()`
* **Alur:**
    1.  `List` sementara yang berisi 2 antrian tadi diserahkan ke `antrianListView`.
    2.  `antrianListView.setItems(...)`
    3.  Layar Anda langsung me-refresh dan hanya menampilkan 2 antrian yang lolos filter.
    4.  **PENTING:** `fullAntrianList` (Master List) **tetap utuh** (masih berisi 100 antrian), siap jika Anda mengubah filter lagi.
