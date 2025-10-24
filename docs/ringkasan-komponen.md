ir, String password)`**: Konstruktor ini menyalin seluruh parameter ke field internal agar objek `User` langsung siap dipakai sebagai representasi data pengguna dasar. Dengan menginisialisasi setiap kolom sejak awal, aplikasi bisa mengandalkan objek ini untuk dipersistensikan ataupun ditampilkan tanpa perlu setter tambahan.
- **`getNik()`**: Mengembalikan NIK yang tersimpan di objek dan sering dipakai sebagai identitas unik ketika mencari pengguna di file. Dengan menyediakan akses read-only, kode lain bisa menggunakannya untuk validasi tanpa risiko mengubah nilai.
- **`getNama()`**: Memberikan nama lengkap pengguna yang ditampilkan pada label sambutan dashboard. Metode ini memastikan tampilan memperoleh data langsung dari model resmi, menjaga konsistensi antar layar.
- **`getAlamat()`**: Menyediakan alamat rumah pengguna, misalnya untuk menyalin data pasien saat pembuatan antrian baru. Karena alamat disimpan sebagai satu string, getter ini menjaga struktur data tetap sederhana.
- **`getNoTelepon()`**: Mengembalikan nomor telepon pengguna yang dipakai saat memprapopulasi formulir ataupun kebutuhan kontak. Dengan getter khusus, controller tidak perlu mengetahui detail penyimpanan internal.
- **`getEmail()`**: Menghasilkan alamat email untuk proses login, pencarian akun, dan validasi duplikasi saat registrasi. Metode ini membantu memastikan seluruh modul membaca data dari sumber yang sama.
- **`getTanggalLahir()`**: Menyediakan tanggal lahir yang disimpan sebagai teks, yang kemudian dapat diformat ulang bila dibutuhkan di UI. Getter ini menjaga kompatibilitas dengan format file sederhana.
- **`getPassword()`**: Memberikan kata sandi untuk proses autentikasi. Dengan hanya menyediakan akses baca, logika login bisa memverifikasi password tanpa bisa memodifikasi nilai rahasia tersebut.
- **`toString()`**: Menggabungkan seluruh data user menjadi satu baris teks dengan pemisah titik koma sehingga siap ditulis ke `users.txt`. Representasi terstandar ini memudahkan proses serialisasi dan kompatibilitas antarmodul penyimpanan.

### `Pasien`
- **`Pasien(..., String noRekamMedis)`**: Konstruktor ini memperluas konstruktor `User` dengan nomor rekam medis tambahan sehingga setiap pasien memiliki identitas medis unik. Dengan memanggil `super`, proses pewarisan menjaga agar data umum dan spesifik tersimpan seragam.
- **`getNoRekamMedis()`**: Mengembalikan kode rekam medis yang digunakan ketika menampilkan data pasien di detail antrian ataupun saat verifikasi internal. Metode ini menjadi penghubung antara domain medis dan data pengguna biasa.
- **`toString()`**: Menambahkan nomor rekam medis dan penanda role `pasien` ke string bawaan `User`, sehingga saat dibaca kembali, `FileService` dapat mengenali bahwa baris ini mewakili pasien. Mekanisme ini memastikan interoperabilitas dengan sistem parsing otomatis.

### `Admin`
- **`Admin(..., String jabatan)`**: Konstruktor ini menginisialisasi akun admin lengkap dengan jabatan sehingga sistem dapat menampilkan peran resmi di antarmuka, misalnya pada laporan internal. Pewarisan dari `User` membantu menghindari duplikasi atribut umum.
- **`getJabatan()`**: Mengembalikan jabatan admin yang bisa dipakai ketika menampilkan profil ataupun untuk log audit. Metode ini menyediakan cara tunggal untuk membaca informasi otoritatif tentang status admin.
- **`toString()`**: Menyisipkan jabatan dan role `admin` ke representasi string sehingga proses pembacaan ulang mengetahui bahwa pengguna memiliki hak istimewa. Dengan begitu, modul login dapat langsung membedakan rute dashboard tanpa logika tambahan.

### `Antrian`
- **`Antrian(int id, String nikPembuat, String nikPasien, String namaPasien, String alamatPasien, String noTeleponPasien, String poli, String keluhan, String status, LocalDateTime timestamp)`**: Konstruktor utama yang menerima seluruh atribut antrian agar objek siap digunakan untuk daftar, detail, maupun penyimpanan file. Karena seluruh data dimasukkan saat pembuatan, controller bisa mengandalkannya untuk menjaga konsistensi status dan informasi pasien.
- **`getId()`**: Mengembalikan identitas numerik antrian yang dipakai sebagai penentu unik di file, tampilan kartu, dan logika penyaringan. Metode ini memungkinkan referensi silang antar modul tanpa kebingungan.
- **`getNikPembuat()`**: Memberi tahu NIK pemilik akun yang membuat antrian sehingga dashboard pasien dapat memfilter daftar hanya untuk dirinya. Informasi ini penting untuk menjaga privasi antar pengguna.
- **`getNikPasien()`**: Menyediakan NIK pasien yang ditampilkan pada detail dan digunakan sebagai acuan ketika migrasi data lama. Dengan getter ini, modul lain bisa mencocokkan pasien secara eksplisit.
- **`getNamaPasien()`**: Menghasilkan nama pasien yang ditampilkan pada setiap kartu antrian serta memudahkan pencarian dengan kata kunci. Hal ini meningkatkan pengalaman pengguna ketika menelusuri daftar panjang.
- **`getAlamatPasien()`**: Mengembalikan alamat pasien sehingga form `BuatAntrian` dapat menyalin atau mengedit data sesuai kebutuhan tanpa menyimpan salinan ganda.
- **`getNoTeleponPasien()`**: Memberikan nomor telepon pasien yang bisa ditampilkan atau diekspor untuk notifikasi manual. Data ini memastikan petugas memiliki cara menghubungi pasien.
- **`getPoli()`**: Mengembalikan poli tujuan yang menjadi dasar filter dan warna status di UI. Dengan memusatkan akses di getter ini, modul lain tidak perlu mengetahui implementasi internal.
- **`getKeluhan()`**: Menyediakan keluhan pasien yang akan di-render di detail maupun disisipkan pada string penyimpanan. Getter ini memastikan teks keluhan tetap utuh setelah melewati berbagai lapisan.
- **`getStatus()`**: Menghasilkan status terbaru antrian yang menentukan indikator warna, aksi yang diizinkan, serta proses otomatis. Dengan memanggil metode ini, controller dapat menyesuaikan UI secara dinamis.
- **`getTimestamp()`**: Memberikan stempel waktu pembuatan sebagai `LocalDateTime`, penting untuk filter mingguan/bulanan dan pemrosesan status otomatis. Data tipe kuat ini memudahkan perhitungan berbasis waktu.
- **`setStatus(String status)`**: Mengubah status antrian sehingga modul admin dapat menandai perkembangan layanan (misalnya menjadi "Selesai"). Setter ini satu-satunya jalan resmi untuk memperbarui status agar tetap terkendali.
- **`getFormattedTimestamp()`**: Mengubah `timestamp` menjadi string ramah baca sehingga tampilan kartu dapat menunjukkan kapan antrian dibuat tanpa logika format di UI. Hal ini menjaga konsistensi format tanggal di seluruh aplikasi.
- **`toString()`**: Menyusun seluruh atribut menjadi satu baris teks dengan menggantikan karakter newline di keluhan sehingga aman disimpan dalam file teks. Representasi ini memastikan data dapat dimuat ulang dengan tepat.
- **`equals(Object o)`**: Membandingkan dua objek berdasarkan ID untuk menentukan apakah keduanya mewakili entri antrian yang sama. Fungsinya penting ketika koleksi atau operasi penyaringan perlu mencegah duplikasi.
- **`hashCode()`**: Menghasilkan hash konsisten berdasarkan ID sehingga objek dapat digunakan sebagai kunci dalam struktur data berbasis hash. Dengan implementasi ini, antrian dapat dimasukkan ke set tanpa perilaku tak terduga.

## Service (`src/com/antrian/core/service`)

### `FileService`
- **Konstanta `USER_FILE` & `ANTRIAN_FILE`**: Menetapkan nama file sumber data sehingga seluruh metode membaca dan menulis ke lokasi konsisten. Menyimpan nama di konstanta mencegah salah ketik dan memudahkan penggantian ketika lokasi file berubah.
- **`loadAndProcessAntrianStatus()`**: Membaca seluruh antrian dari disk, memeriksa usia setiap entri, dan mengubah status "Baru" menjadi "Sedang Berlangsung" apabila telah lebih dari satu menit. Setelah perubahan, daftar disimpan kembali sehingga pembaruan status otomatis tercatat permanen sebelum data dikembalikan ke pemanggil.
- **`clearAntrianFile()`**: Membuka file antrian dalam mode tulis kosong untuk menghapus semua baris secara cepat. Fungsionalitas ini dipakai tombol reset admin agar seluruh data benar-benar dibersihkan tanpa menyisakan baris lama.
- **`loadUsers()`**: Menghasilkan daftar `User` dengan membaca setiap baris `users.txt`, memparsing, dan mengabaikan data rusak. Karena menggunakan stream, metode ini efisien dan menjaga hasil selalu berupa objek valid.
- **`saveUsers(List<User> users)`**: Mengubah tiap objek pengguna menjadi string via `toString()` kemudian menulis ulang file `users.txt`. Dengan memaksa penulisan ulang total, file selalu sinkron dengan keadaan terbaru aplikasi.
- **`loadAntrian()`**: Membaca `antrian.txt` dan mengonversi setiap baris menjadi objek `Antrian` dengan deteksi format lama maupun baru. Hal ini memungkinkan kompatibilitas mundur tanpa memerlukan skrip migrasi terpisah.
- **`saveAntrian(List<Antrian> antrianList)`**: Menyimpan seluruh daftar antrian ke disk menggunakan representasi string standar. Karena metode ini memanggil `writeLines`, proses output terpusat sehingga mudah diuji.
- **`getNextAntrianId()`**: Mengambil seluruh antrian, mencari ID tertinggi, lalu menambah satu untuk memastikan setiap entri baru memiliki identitas unik. Bila file kosong, metode ini otomatis memulai dari ID 1 sehingga tidak perlu konfigurasi tambahan.
- **`writeLines(String fileName, List<String> lines)`** *(paket privat)*: Menulis baris teks ke file menggunakan `PrintWriter` dalam satu transaksi, memastikan file tidak dibiarkan dalam keadaan setengah jadi. Metode utilitas ini menjadi fondasi bagi `saveUsers` dan `saveAntrian`.
- **`parseUser(String line)`** *(private)*: Memecah baris pengguna berdasarkan titik koma dan membuat objek `Admin` atau `Pasien` sesuai penanda role. Fungsi ini juga menangani pengecualian parsing dengan melaporkan error ke log tanpa menghentikan proses pemuatan keseluruhan.
- **`parseAntrian(String line)`** *(private)*: Mengonversi baris antrian menjadi objek `Antrian`, termasuk migrasi otomatis format lama yang tidak memiliki kolom NIK pembuat. Dengan strategi ini, aplikasi tetap bisa membaca arsip lama sambil mencatat kasus yang perlu perhatian.

## UI Utama (`src/com/antrian/ui`)

### `Main`
- **`start(Stage primaryStage)`**: Titik awal JavaFX yang memuat `Login.fxml`, memasang ikon aplikasi, dan menampilkan jendela utama dalam keadaan dapat diubah ukurannya. Metode ini juga menyimpan referensi stage pada variabel statis agar dapat dipakai untuk pergantian scene di seluruh aplikasi.
- **`changeScene(String fxml, double width, double height)`**: Mengganti konten stage aktif dengan layout baru berdasarkan jalur FXML, memungkinkan perpindahan antar halaman (login, dashboard, dsb.) tanpa membuat jendela baru. Parameter lebar dan tinggi disediakan untuk konsistensi, meskipun root akan mengisi stage yang sama.
- **`getStage()`**: Mengembalikan instance `Stage` utama yang digunakan ketika controller membutuhkan akses langsung, misalnya membuka dialog modal. Dengan menyediakan metode statis, kode lain tidak perlu menyimpan referensi sendiri-sendiri.
- **`main(String[] args)`**: Memanggil `launch()` JavaFX sehingga aplikasi desktop dapat dijalankan dari command line atau IDE. Fungsi ini hanya bertugas sebagai delegasi ke lifecycle JavaFX standar.

## Controller (`src/com/antrian/ui/controller`)

### `BaseDashboardController`
- **`initialize()`**: Dipanggil otomatis oleh JavaFX untuk menyetel label sambutan berdasarkan pengguna yang login, menyiapkan kontrol filter, membangun tampilan list view, memuat data awal, dan memulai auto-refresh. Rangkaian langkah ini memastikan dashboard langsung responsif begitu ditampilkan.
- **`startAutoRefresh()`** *(private)*: Membuat `Timeline` yang memicu `loadAntrianData()` setiap lima detik sehingga daftar selalu sinkron dengan data di file. Timeline dimainkan terus menerus, dan akan dihentikan sementara ketika dialog detail dibuka.
- **`setupFilterControls()`** *(private)*: Mengisi pilihan default untuk filter poli dan status, serta memasang listener pada setiap kontrol dan kolom pencarian. Setiap perubahan langsung memanggil `applyFilters()` agar pengguna melihat hasil penyaringan secara real time.
- **`applyFilters()`** *(protected)*: Menyaring `fullAntrianList` berdasarkan nama pasien, poli, dan status lalu memperbarui `ListView` tanpa menghilangkan item terpilih. Metode ini di-override admin untuk menambahkan filter tanggal tetapi memberikan implementasi dasar yang dapat digunakan pasien.
- **`setupListViewFactory()`** *(private)*: Mengatur cell factory `ListView` agar setiap antrian dirender sebagai kartu dengan indikator warna, judul, meta-data, dan tombol detail khusus admin. Dengan desain ini, tampilan menjadi lebih informatif dibandingkan teks polos.
- **`showDetailAntrian(Antrian antrian)`** *(protected)*: Menjeda auto-refresh, memuat `AntrianDetail.fxml` dalam dialog modal, dan meneruskan callback untuk memuat ulang data setelah dialog ditutup. Fungsi ini memastikan pengalaman melihat detail tidak menggangu pembaruan berkala tetapi tetap menjaga sinkronisasi.
- **`getStatusColor(String status)`** *(private)*: Mengonversi status antrian ke warna `Color` tertentu agar indikator visual konsisten. Penanganan status tidak dikenal menghasilkan warna abu-abu sehingga UI tetap terkontrol.
- **`loadAntrianData()`** *(protected, abstrak)*: Dibiarkan kosong untuk diwajibkan pada subclass karena admin dan pasien memiliki sumber data berbeda. Pola ini memanfaatkan template method pattern agar logika umum tetap berada di kelas dasar.
- **`handleLogout()`** *(protected)*: Menghentikan `Timeline`, menghapus sesi pengguna, dan memanggil `Main.changeScene` untuk kembali ke halaman login. Dengan memusatkan logika di sini, setiap dashboard mendapatkan perilaku logout yang konsisten.

### `AdminDashboardController`
- **`initialize()`**: Memperluas inisialisasi dasar dengan menambahkan opsi filter waktu (Semua Waktu, Minggu Ini, Bulan Ini, Tahun Ini) dan listenernya. Langkah ini memberi admin perspektif rentang waktu tanpa perlu menulis ulang logika lain.
- **`loadAntrianData()`**: Mengambil seluruh antrian menggunakan `FileService.loadAndProcessAntrianStatus()`, menyimpan ke `fullAntrianList`, kemudian memanggil `applyFilters()`. Dengan cara ini dashboard selalu menampilkan status terbaru sebelum filter diterapkan.
- **`applyFilters()`**: Mengoverride metode dasar untuk menambahkan filter tanggal berdasarkan minggu, bulan, atau tahun menggunakan API tanggal Java. Setelah penyaringan selesai, metode juga mengembalikan seleksi sebelumnya agar interaksi admin tidak terganggu.
- **`handleResetAntrian()`**: Menampilkan dialog konfirmasi, dan jika disetujui, memanggil `FileService.clearAntrianFile()` lalu memuat ulang data. Fitur ini memberi admin kemampuan membersihkan sistem sambil memberikan umpan balik error apabila operasi file gagal.

### `PatientDashboardController`
- **`loadAntrianData()`**: Mengambil seluruh antrian, lalu memfilter hanya yang dibuat oleh pasien yang sedang login menggunakan `nikPembuat`. Setelah daftar diperbarui, `applyFilters()` dipanggil agar filter UI tetap berlaku.
- **`handleBuatAntrian()`**: Membuka dialog `BuatAntrian.fxml`, memberikan callback `loadAntrianData()` ke controller dialog, dan menunggu hingga dialog ditutup. Cara ini memastikan begitu pasien menambahkan antrian, daftar utama langsung terbarui tanpa perlu menyegarkan manual.

### `LoginController`
- **`handleLoginButtonAction()`**: Memvalidasi isian email/nama dan password, memuat data user dari file, lalu mencari kecocokan menggunakan stream. Jika ditemukan, metode menetapkan `Main.loggedInUser` dan mengarahkan ke dashboard sesuai peran; jika tidak, ia menampilkan pesan kesalahan yang relevan.
- **`goToRegister()`**: Mengganti scene ke `Register.fxml` sehingga pengguna yang belum punya akun dapat mendaftar. Fungsi ini memanfaatkan utilitas `Main` agar transisi antar tampilan konsisten.

### `RegisterController`
- **`handleDaftarButtonAction()`**: Memastikan seluruh field terisi, memformat tanggal lahir, membuat objek `Pasien`, dan memeriksa apakah NIK atau email sudah digunakan. Jika valid, data disimpan ke file, dialog informasi sukses muncul, dan pengguna dialihkan ke halaman login.
- **`goToLogin()`**: Mengarahkan kembali ke halaman login, biasanya setelah pengguna menyadari sudah memiliki akun atau selesai registrasi. Dengan metode ini, UI memiliki satu jalur konsisten untuk kembali.

### `BuatAntrianController`
- **`setCallback(Runnable callback)`**: Menyimpan fungsi yang akan dipanggil setelah antrian baru tersimpan sehingga dashboard pemanggil dapat memperbarui daftar. Tanpa callback ini, pasien harus menyegarkan data secara manual.
- **`initialize()`**: Mengisi `ComboBox` poli dengan daftar layanan yang tersedia saat dialog dibuka. Dengan mempersiapkan pilihan sejak awal, pengguna dapat langsung memilih tanpa menunggu interaksi lain.
- **`handleAmbilAntrian()`**: Melakukan validasi semua field, meminta ID baru ke `FileService`, membuat objek `Antrian`, menambahkannya ke daftar yang sudah ada, lalu menyimpan kembali ke file. Setelah berhasil, metode ini menampilkan pesan sukses, menjalankan callback, dan menutup jendela agar pengalaman pengguna ringkas.
- **`showAlert(Alert.AlertType type, String title, String message)`** *(private)*: Membungkus pembuatan `Alert` sehingga pesan sukses maupun error dapat ditampilkan dengan mudah dan konsisten. Dengan utilitas ini, kode utama `handleAmbilAntrian` tetap bersih.
- **`closeWindow()`** *(private)*: Mengambil stage dari elemen UI dan menutup dialog setelah operasi selesai untuk mencegah pengguna melakukan perubahan ganda. Metode ini dipanggil setelah callback berjalan.

### `AntrianDetailController`
- **`initData(Antrian antrian, Runnable callback)`**: Menyimpan objek antrian yang akan ditampilkan, mengatur hak akses tombol berdasarkan peran (admin/pasien), mengisi field awal, dan memulai auto-refresh. Pendekatan ini memastikan detail yang dilihat selalu relevan dengan hak pengguna.
- **`populateFields(Antrian antrian)`** *(private)*: Memindahkan nilai dari objek antrian ke seluruh kontrol UI, termasuk menyiapkan pilihan status jika belum ada. Dengan satu metode khusus, pembaruan otomatis maupun manual dapat menggunakan logika yang sama.
- **`startAutoRefresh()`** *(private)*: Membuat `Timeline` tiga detik sekali untuk memanggil `refreshData()`, sehingga tampilan detail mengikuti perubahan status yang mungkin dilakukan dari tempat lain. Hal ini penting untuk menjaga konsistensi ketika beberapa admin bekerja bersamaan.
- **`refreshData()`** *(private)*: Memuat ulang daftar antrian dari file, mencari entri yang sesuai, dan memperbarui tampilan atau menutup jendela jika antrian sudah dihapus. Metode ini bertindak sebagai sinkronisasi antara view dan sumber data.
- **`handleSimpan()`**: Mengambil daftar antrian, mencari entri yang cocok, memperbarui status sesuai pilihan, dan menyimpan ulang file. Setelah berhasil, muncul notifikasi informasi dan jendela ditutup sehingga perubahan langsung tercermin di dashboard.
- **`handleHapus()`**: Memunculkan dialog konfirmasi, dan jika disetujui, menghapus entri dari daftar antrian sebelum menyimpannya kembali. Pesan sukses ditampilkan setelah operasi selesai sehingga admin tahu aksi berhasil.
- **`handleKembali()`**: Menutup jendela detail tanpa melakukan perubahan dan memanggil callback untuk memastikan daftar utama tetap segar. Fungsi ini memberi jalan keluar aman bagi pengguna yang hanya ingin melihat informasi.
- **`closeWindow()`** *(private)*: Menghentikan auto-refresh, menjalankan callback bila ada, lalu menutup stage agar tidak ada update tambahan setelah jendela tertutup. Metode ini digunakan oleh beberapa aksi penutup untuk menjaga perilaku konsisten.
- **`showAlert(Alert.AlertType type, String title, String content)`** *(private)*: Membantu menampilkan pesan error maupun informasi dengan format seragam sehingga pengguna langsung memahami hasil tindakan.

## Tampilan FXML (`resources/fxml`)

- **`Login.fxml`**: Membentuk tampilan login berbasis `StackPane` dengan `VBox` berisi logo, judul, input email/nama dan password, tombol masuk, tautan ke registrasi, serta label kesalahan. Struktur ini menyediakan alur vertikal yang mudah diikuti pengguna, sementara stylesheet eksternal menjaga konsistensi visual.
- **`Register.fxml`**: Menggunakan `StackPane` dan `VBox` untuk membungkus `GridPane` dua kolom yang menampung field NIK, nama, alamat, nomor telepon, email, tanggal lahir, dan password. Di bagian bawah terdapat tombol daftar, tautan kembali ke login, dan label error sehingga proses pendaftaran berlangsung di satu layar.
- **`AdminDashboard.fxml`**: Memadukan `VBox` utama dengan `HBox` header berisi salam, tombol reset, dan logout, diikuti baris filter (pencarian, poli, status, rentang waktu) serta `ListView` untuk daftar antrian. Tata letak ini memprioritaskan kontrol administrasi di bagian atas dan daftar data di area konten.
- **`PatientDashboard.fxml`**: Menampilkan salam pengguna, tombol logout, judul "Antrian Anda", tombol "Buat Antrian Baru", baris filter nama/poli/status, dan `ListView` hasil filter. Fokus desain diarahkan pada akses cepat ke pembuatan antrian sambil tetap memberikan visibilitas daftar yang sudah ada.
- **`BuatAntrian.fxml`**: Dialog berbasis `VBox` dengan `GridPane` yang menampung input identitas pasien dan poli, textarea keluhan, serta tombol "Ambil Antrian" di sisi kanan bawah. Label informasi di bagian akhir menampilkan nomor antrian yang berhasil dibuat sehingga pasien langsung mengetahui hasilnya.
- **`AntrianDetail.fxml`**: Layout `VBox` yang menampilkan ID antrian besar di atas, `GridPane` tiga baris untuk data pasien, `ComboBox` status (editable untuk admin), textarea keluhan read-only, dan tombol aksi (Hapus, Simpan, Kembali). Struktur ini memisahkan informasi dan tindakan dengan jelas sehingga admin dapat bekerja cepat tanpa kehilangan konteks.

---
Dokumentasi ini dapat dijadikan referensi mendalam ketika melakukan debugging, menambah fitur, atau menjelaskan alur kerja kepada anggota tim baru karena setiap fungsi dijabarkan dengan perannya dalam aplikasi.
