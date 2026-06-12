package com.example.praktam_2417051045.data.model

import com.example.praktam_2417051045.R

object DecisionSource {
    val dummyDecision = listOf(
        Decision(
            title = "Jalur Karir",
            description = "Pilih antara stabilitas kantoran atau kebebasan freelance. Langkah besar untuk masa depan finansialmu.",
            category = "Career",
            imageRes = R.drawable.career,
            imageUrl = "https://images.unsplash.com/photo-1521737604893-d14cc237f11d?auto=format&fit=crop&w=800&q=60",
            pros = listOf("Gaji stabil tiap bulan", "Asuransi & tunjangan lengkap", "Jenjang karir jelas"),
            cons = listOf("Waktu kerja kaku", "Risiko burnout tinggi", "Rutinitas monoton"),
            tips = listOf("Cek standar gaji industri", "Pastikan budaya kantor cocok", "Siapkan dana darurat"),
            analysis = "Sangat disarankan jika kamu mengejar keamanan finansial jangka panjang.",
            suitableFor = listOf("Mahasiswa", "Pekerja")
        ),
        Decision(
            title = "Lanjut S2",
            description = "Pendalaman ilmu spesifik. Cocok untuk kamu yang ingin menjadi ahli atau dosen.",
            category = "Education",
            imageRes = R.drawable.study,
            imageUrl = "https://images.unsplash.com/photo-1498050108023-c5249f4df085?auto=format&fit=crop&w=800&q=60",
            pros = listOf("Kualifikasi lebih tinggi", "Networking akademik luas", "Peluang riset mendalam"),
            cons = listOf("Biaya kuliah mahal", "Menunda cari duit", "Tekanan tugas berat"),
            tips = listOf("Berburu beasiswa penuh", "Pilih jurusan yang linear", "Publikasi karya ilmiah"),
            analysis = "Pilihan cerdas untuk investasi otak, asalkan rencana biaya sudah matang.",
            suitableFor = listOf("Mahasiswa")
        ),
        Decision(
            title = "Buka Bisnis",
            description = "Membangun usaha dari nol. Jadilah bos untuk diri sendiri dengan risiko yang menantang.",
            category = "Business",
            imageRes = R.drawable.business,
            imageUrl = "https://images.unsplash.com/photo-1556745757-8d76bdb6984b?auto=format&fit=crop&w=800&q=60",
            pros = listOf("Waktu fleksibel", "Profit tanpa batas", "Bangun aset sendiri"),
            cons = listOf("Income tidak menentu", "Tanggung jawab full", "Modal awal besar"),
            tips = listOf("Mulai dari skala kecil", "Disiplin arus kas", "Terus inovasi produk"),
            analysis = "Cocok untuk jiwa petualang yang siap kerja keras demi hasil besar.",
            suitableFor = listOf("Wirausaha", "Lulusan SMA")
        ),
        Decision(
            title = "Solo Travel",
            description = "Jelajahi tempat baru sendirian. Cara terbaik untuk mengenal diri sendiri lebih dalam.",
            category = "Lifestyle",
            imageRes = R.drawable.travel,
            imageUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=800&q=60",
            pros = listOf("Bebas atur jadwal", "Latih kemandirian", "Teman baru mendunia"),
            cons = listOf("Risiko keamanan", "Biaya lebih mahal", "Kadang merasa sepi"),
            tips = listOf("Share lokasi ke keluarga", "Simpan uang cadangan", "Gunakan navigasi akurat"),
            analysis = "Pengalaman hidup yang tak ternilai. Persiapkan keamanan dengan sangat teliti.",
            suitableFor = listOf("Umum", "Mahasiswa", "Lulusan SMA")
        )
    )
}
