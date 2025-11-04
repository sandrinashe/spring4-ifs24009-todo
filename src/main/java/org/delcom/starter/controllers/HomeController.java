package org.delcom.starter.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class HomeController {

    @GetMapping("/")
    public String hello() {
        return "Hay Abdullah, selamat datang di pengembangan aplikasi dengan Spring Boot!";
    }

    @GetMapping("/hello/{nama}")
    public String sayHello(@PathVariable String nama) {
        return "Hello, " + nama + "!";
    }

    @GetMapping("/informasi-nim")
public String informasiNim(@RequestParam String nim) {
    if (nim == null || nim.length() != 8) {
        return "NIM harus 8 karakter";
    }

    Map<String, String> prodiMap = new HashMap<>();
    prodiMap.put("11", "Sarjana Informatika");
    prodiMap.put("12", "Sarjana Sistem Informasi");
    prodiMap.put("14", "Sarjana Teknik Elektro");
    prodiMap.put("21", "Sarjana Manajemen Rekayasa");
    prodiMap.put("22", "Sarjana Teknik Metalurgi");
    prodiMap.put("31", "Sarjana Teknik Bioproses");
    prodiMap.put("114", "Diploma 4 Teknologi Rekasaya Perangkat Lunak");
    prodiMap.put("113", "Diploma 3 Teknologi Informasi");
    prodiMap.put("133", "Diploma 3 Teknologi Komputer");

    String namaProdi = null;
    
    // Cek kode prodi 3 digit terlebih dahulu
    if (nim.length() >= 3) {
        String kodeProdi3Digit = nim.substring(0, 3);
        namaProdi = prodiMap.get(kodeProdi3Digit);
    }
    
    // Jika tidak ketemu, cek kode prodi 2 digit
    if (namaProdi == null && nim.length() >= 2) {
        String kodeProdi2Digit = nim.substring(0, 2);
        namaProdi = prodiMap.get(kodeProdi2Digit);
    }

    if (namaProdi == null) {
        return "Program Studi tidak Tersedia";
    }

    // Ambil tahun angkatan dan urutan berdasarkan panjang NIM
    String tahunAngkatan, urutan;
    if (namaProdi.startsWith("Diploma")) {
        // Untuk kode prodi 3 digit: NIM = 11407777
        // 114 = kode prodi, 07 = tahun, 777 = urutan
        tahunAngkatan = nim.substring(3, 5);
        urutan = nim.substring(5, 8);
    } else {
        // Untuk kode prodi 2 digit: NIM = 11234567  
        // 11 = kode prodi, 23 = tahun, 4567 = urutan
        tahunAngkatan = nim.substring(2, 4);
        urutan = nim.substring(4, 8);
    }

    String angkatan = "20" + tahunAngkatan;
    return "Informasi NIM " + nim + ": >> Program Studi: " + namaProdi +
           " >> Angkatan: " + angkatan + " >> Urutan: " + urutan;
}

    @GetMapping("/perolehan-nilai")
    public String perolehanNilai(
            @RequestParam double wPA,
            @RequestParam double wT,
            @RequestParam double wK,
            @RequestParam double wP,
            @RequestParam double wUTS,
            @RequestParam double wUAS,
            @RequestParam String data) {

        try {
            if (data == null || data.trim().isEmpty()) {
                return "Error menghitung nilai: Data tidak boleh kosong";
            }

            Scanner scanner = new Scanner(data);
            double totalNilai = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("\\|");
                if (parts.length == 3) {
                    double bobot = Double.parseDouble(parts[1]);
                    double nilai = Double.parseDouble(parts[2]);
                    totalNilai += (bobot * nilai / 100);
                }
            }
            scanner.close();

            String grade;
            if (totalNilai >= 85) {
                grade = "A";
            } else if (totalNilai >= 70) {
                grade = "B";
            } else if (totalNilai >= 55) {
                grade = "C";
            } else if (totalNilai >= 40) {
                grade = "D";
            } else {
                grade = "E";
            }

            return "Nilai Akhir: " + String.format("%.2f", totalNilai) + " >> Grade: " + grade;

        } catch (Exception e) {
            return "Error menghitung nilai: " + e.getMessage();
        }
    }

    @GetMapping("/perbedaan-l")
    public String perbedaanL(@RequestParam String data) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(data);
            String decodedString = new String(decodedBytes);

            Scanner scanner = new Scanner(decodedString);
            int n = scanner.nextInt();
            int[][] matrix = new int[n][n];

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    matrix[i][j] = scanner.nextInt();
                }
            }
            scanner.close();

            int nilaiL = 0;
            int nilaiKebalikanL = 0;
            int nilaiTengah = 0;

            if (n >= 3) {
                // Hitung nilai L (diagonal kiri + kolom pertama)
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j <= i; j++) {
                        if (i == j || j == 0) {
                            nilaiL += matrix[i][j];
                        }
                    }
                }

                // Hitung nilai kebalikan L (diagonal kanan + kolom terakhir)
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        if (i == j || j == n - 1) {
                            nilaiKebalikanL += matrix[i][j];
                        }
                    }
                }

                // Hitung nilai tengah
                int[] allElements = new int[n * n];
                int index = 0;
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        allElements[index++] = matrix[i][j];
                    }
                }
                Arrays.sort(allElements);
                nilaiTengah = allElements[allElements.length / 2];
            } else {
                // Untuk matrix kecil (< 3x3), gunakan logika sederhana
                int sum = 0;
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        sum += matrix[i][j];
                    }
                }
                nilaiL = sum;
                nilaiKebalikanL = sum;

                // Hitung nilai tengah
                int[] allElements = new int[n * n];
                int index = 0;
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        allElements[index++] = matrix[i][j];
                    }
                }
                Arrays.sort(allElements);
                nilaiTengah = allElements[allElements.length / 2];
            }

            int perbedaan = Math.abs(nilaiL - nilaiKebalikanL);
            int dominan = (nilaiL >= nilaiKebalikanL) ? nilaiL : nilaiKebalikanL;

            return "Nilai L: " + (n >= 3 ? nilaiL : "Tidak Ada") + "<br/>" +
                   "Nilai Kebalikan L: " + (n >= 3 ? nilaiKebalikanL : "Tidak Ada") + "<br/>" +
                   "Nilai Tengah: " + nilaiTengah + "<br/>" +
                   "Perbedaan: " + (n >= 3 ? perbedaan : "Tidak Ada") + "<br/>" +
                   "Dominan: " + dominan;

        } catch (Exception e) {
            return "Error processing matrix data: " + e.getMessage();
        }
    }

    @GetMapping("/paling-ter")
    public String palingTer(@RequestParam String data) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(data);
            String decodedString = new String(decodedBytes);

            Scanner scanner = new Scanner(decodedString);
            List<Integer> numbers = new ArrayList<>();

            while (scanner.hasNext()) {
                if (scanner.hasNextInt()) {
                    numbers.add(scanner.nextInt());
                } else {
                    scanner.next();
                }
            }
            scanner.close();

            if (numbers.isEmpty()) {
                return "Informasi tidak tersedia";
            }

            Map<Integer, Integer> frequencyMap = new HashMap<>();
            for (int num : numbers) {
                frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
            }

            int tertinggi = Collections.max(numbers);
            int terendah = Collections.min(numbers);

            int maxFrequency = Collections.max(frequencyMap.values());
            int minFrequency = Collections.min(frequencyMap.values());

            List<Integer> terbanyak = new ArrayList<>();
            List<Integer> tersedikit = new ArrayList<>();

            for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
                if (entry.getValue() == maxFrequency) {
                    terbanyak.add(entry.getKey());
                }
                if (entry.getValue() == minFrequency) {
                    tersedikit.add(entry.getKey());
                }
            }

            Collections.sort(terbanyak);
            Collections.sort(tersedikit);

            int nilaiTerbanyak = terbanyak.get(terbanyak.size() - 1);
            int nilaiTersedikit = tersedikit.get(0);

            int jumlahTertinggi = nilaiTerbanyak * maxFrequency;
            int jumlahTerendah = nilaiTersedikit * minFrequency;

            return "Tertinggi: " + tertinggi + "<br/>" +
                   "Terendah: " + terendah + "<br/>" +
                   "Terbanyak: " + nilaiTerbanyak + " (" + maxFrequency + "x)<br/>" +
                   "Tersedikit: " + nilaiTersedikit + " (" + minFrequency + "x)<br/>" +
                   "Jumlah Tertinggi: " + nilaiTerbanyak + " * " + maxFrequency + " = " + jumlahTertinggi + "<br/>" +
                   "Jumlah Terendah: " + nilaiTersedikit + " * " + minFrequency + " = " + jumlahTerendah;

        } catch (Exception e) {
            return "Error processing data: " + e.getMessage();
        }
    }
}