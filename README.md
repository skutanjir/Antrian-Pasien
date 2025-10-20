# Aplikasi Antrian Pasien (Desktop)

Aplikasi desktop sederhana untuk manajemen antrian pasien di sebuah klinik atau fasilitas kesehatan. Dibangun menggunakan JavaFX, aplikasi ini menyediakan dua peran utama: **Admin** untuk mengelola seluruh antrian dan **Pasien** untuk mendaftar dan membuat antrian baru.

## ✨ Fitur Utama

-   **Manajemen Pengguna & Peran**
    -   Sistem registrasi dan login untuk pengguna.
    -   Pengecekan NIK unik untuk mencegah duplikasi data pasien.
    -   Dua peran berbeda: `Admin` dan `Pasien` dengan tampilan dan hak akses yang berbeda.

-   **Sistem Antrian**
    -   Penomoran antrian yang berkelanjutan (tidak reset saat aplikasi ditutup).
    -   Empat status antrian: `Baru` (Biru), `Sedang Berlangsung` (Oranye), `Selesai` (Hijau), `Batal` (Merah).
    -   Data antrian dan pengguna disimpan secara persisten dalam file `.txt`.

-   **Fitur Admin**
    -   Melihat **semua** data antrian dari semua pasien.
    -   Melakukan pencarian dan filter antrian berdasarkan:
        -   Nama Pasien
        -   Poli
        -   Status Antrian
        -   Rentang Waktu (Minggu Ini, Bulan Ini, Tahun Ini).
    -   Melihat detail, mengubah status, dan menghapus antrian.
    -   Fitur **Reset Data** untuk mengosongkan semua riwayat antrian.

-   **Fitur Pasien**
    -   Melihat daftar antrian yang **dibuat olehnya**.
    -   Membuat antrian baru dengan form manual (bisa untuk diri sendiri atau orang lain).
    -   Melakukan pencarian dan filter pada antriannya sendiri.

-   **UI/UX & Fitur Otomatis**
    -   Desain modern, bersih, dan responsif yang akan terbuka dalam mode *maximize*.
    -   Tampilan daftar antrian berbentuk "Card" yang dinamis.
    -   **Auto-Refresh**: Dashboard dan halaman detail akan memperbarui data secara otomatis setiap beberapa detik.
    -   **Status Otomatis**: Antrian "Baru" akan otomatis berubah menjadi "Sedang Berlangsung" setelah 1 menit.

-   **Ketahanan Program (Robustness)**
    -   Aplikasi dapat menangani format data lama dan baru di `antrian.txt` secara otomatis tanpa menyebabkan error, sehingga tidak perlu menghapus file manual.

## 🛠️ Teknologi yang Digunakan

-   **Java 17+**
-   **JavaFX 21+** (untuk GUI)
-   **CSS** (untuk styling tampilan modern)

## ⚙️ Cara Setup dan Instalasi

#### 1. Prasyarat
-   Pastikan Anda memiliki **JDK (Java Development Kit)** versi 17 atau lebih baru.
-   Download **JavaFX SDK** sesuai dengan versi JDK dan sistem operasi Anda.

#### 2. Struktur Proyek
Pastikan proyek Anda memiliki struktur folder seperti berikut:
AntrianApp/
├── src/
│   └── com/antrian/...
├── resources/
│   ├── css/
│   │   └── style.css
│   └── fxml/
│       └── (Semua file .fxml)
├── users.txt
└── antrian.txt
#### 3. Setup Awal
1.  Letakkan semua file kode (`.java`, `.fxml`, `.css`) sesuai dengan struktur di atas.
2.  Buat dua file kosong di folder utama proyek: `users.txt` dan `antrian.txt`.
3.  Untuk bisa login sebagai admin pertama kali, masukkan baris berikut ke dalam file `users.txt`:
    ```
    000;Admin;Kantor;08123;admin@app.com;01/01/2000;admin123;admin
    ```

## 🚀 Cara Menjalankan Aplikasi

#### 1. Melalui IDE (IntelliJ IDEA / Eclipse)
1.  Buka proyek di IDE Anda.
2.  Tambahkan JavaFX SDK sebagai library global atau library proyek.
3.  Buat *Run Configuration* baru untuk kelas `com.antrian.ui.Main`.
4.  Tambahkan argumen VM berikut di Run Configuration tersebut. **Sesuaikan path ke JavaFX SDK Anda**.
    ```
    --module-path "C:\path\to\your\javafx-sdk-21\lib" --add-modules javafx.controls,javafx.fxml --enable-native-access=javafx.graphics
    ```
5.  Jalankan aplikasi.

#### 2. Melalui Command Line (PowerShell)
Buka PowerShell, masuk ke folder utama proyek, dan jalankan perintah-perintah ini secara berurutan.

1.  **Atur variabel path JavaFX (wajib setiap sesi baru):**
    ```powershell
    $env:JFX_PATH = "C:\path\to\your\javafx-sdk-21\lib"
    ```

2.  **Kompilasi kode:**
    ```powershell
    javac -d bin --module-path $env:JFX_PATH --add-modules javafx.controls,javafx.fxml src/com/antrian/core/model/*.java src/com/antrian/core/service/*.java src/com/antrian/ui/*.java src/com/antrian/ui/controller/*.java
    ```

3.  **Salin file resources (FXML & CSS):**
    ```powershell
    Copy-Item -Path resources\fxml -Destination bin -Recurse
    Copy-Item -Path resources\css -Destination bin -Recurse
    ```

4.  **Jalankan aplikasi:**
    ```powershell
    java --module-path $env:JFX_PATH --add-modules javafx.controls,javafx.fxml --enable-native-access=javafx.graphics -cp bin com.antrian.ui.Main
    ```

## 👤 Author

- **[Sulistyo Fajar Pratama]**
