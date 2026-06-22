import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HospitalQueue {

    Queue<Patient> normal = new LinkedList<>();

    PriorityQueue<Patient> emergency =
            new PriorityQueue<>(
                    (a, b) -> {
                        if (a.urgency != b.urgency) {
                            return b.urgency - a.urgency;
                        }
                        return Long.compare(a.waktuMasuk, b.waktuMasuk);
                    }
            );

    ArrayList<Patient> selesai = new ArrayList<>();

    long totalWait = 0;
    long longestWait = 0;
    String longestPatient = "none";

    public void tambahPasien(Patient p) {
        if (p.urgency >= 3) {
            emergency.add(p);
            System.out.println("✅ Pasien EMERGENCY masuk: " + p.nama
                    + " (urgency " + p.urgency + ")");
        } else {
            normal.add(p);
            System.out.println("✅ Pasien normal masuk: " + p.nama
                    + " (urgency " + p.urgency + ")");
        }
    }

    public void panggilPasien() {
        Patient p = null;

        if (!emergency.isEmpty()) {
            p = emergency.poll();
        } else if (!normal.isEmpty()) {
            p = normal.poll();
        }

        if (p == null) {
            System.out.println("⚠ Tidak ada pasien dalam antrian.");
            return;
        }

        long wait = (System.currentTimeMillis() - p.waktuMasuk) / 1000;
        totalWait += wait;

        if (wait > longestWait) {
            longestWait = wait;
            longestPatient = p.nama;
        }

        selesai.add(p);
        simpanRiwayat(p, wait);

        System.out.println("\n📢 Memanggil pasien:");
        System.out.println("   " + p);
        System.out.println("   Waktu tunggu: " + wait + " detik");
    }

    public void lihatQueue() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║         STATUS ANTRIAN               ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║  🚨 EMERGENCY QUEUE                  ║");
        System.out.println("╟──────────────────────────────────────╢");

        if (emergency.isEmpty()) {
            System.out.println("║  (kosong)                            ║");
        } else {
            List<Patient> tempList = new ArrayList<>(emergency);
            tempList.sort((a, b) -> {
                if (a.urgency != b.urgency) return b.urgency - a.urgency;
                return Long.compare(a.waktuMasuk, b.waktuMasuk);
            });
            for (Patient p : tempList) {
                long tunggu = (System.currentTimeMillis() - p.waktuMasuk) / 1000;
                System.out.printf("║  %-20s urgency %d  %3ds ║%n",
                        p.nama, p.urgency, tunggu);
            }
        }

        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║  👤 NORMAL QUEUE                     ║");
        System.out.println("╟──────────────────────────────────────╢");

        if (normal.isEmpty()) {
            System.out.println("║  (kosong)                            ║");
        } else {
            for (Patient p : normal) {
                long tunggu = (System.currentTimeMillis() - p.waktuMasuk) / 1000;
                System.out.printf("║  %-20s urgency %d  %3ds ║%n",
                        p.nama, p.urgency, tunggu);
            }
        }

        System.out.println("╚══════════════════════════════════════╝");
    }

    public void realTimeMonitor() {
        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│  ⏱  REAL-TIME QUEUE MONITOR          │");
        System.out.println("├─────────────────────────────────────┤");
        System.out.printf("│  Emergency : %-3d pasien              │%n", emergency.size());
        System.out.printf("│  Normal    : %-3d pasien              │%n", normal.size());
        System.out.printf("│  Selesai   : %-3d pasien              │%n", selesai.size());

        if (!emergency.isEmpty()) {
            Patient top = emergency.peek();
            long tunggu = (System.currentTimeMillis() - top.waktuMasuk) / 1000;
            String info = top.nama + " (" + tunggu + "s)";
            System.out.printf("│  🚨 Next emergency: %-16s│%n", info);
        }

        // FIX: hindari unsafe cast, gunakan iterator langsung
        if (!normal.isEmpty()) {
            Patient top = normal.peek(); // Queue.peek() sudah cukup, tidak perlu cast
            long tunggu = (System.currentTimeMillis() - top.waktuMasuk) / 1000;
            String info = top.nama + " (" + tunggu + "s)";
            System.out.printf("│  👤 Next normal   : %-16s│%n", info);
        }

        if (emergency.isEmpty() && normal.isEmpty()) {
            System.out.println("│  ✅ Antrian kosong                   │");
        }

        System.out.println("└─────────────────────────────────────┘");
    }

    public void statistik() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║            STATISTIK                 ║");
        System.out.println("╠══════════════════════════════════════╣");

        int total = normal.size() + emergency.size() + selesai.size();
        System.out.printf("║  Total pasien       : %-15d║%n", total);
        System.out.printf("║  Sudah diproses     : %-15d║%n", selesai.size());
        System.out.printf("║  Masih antri        : %-15d║%n", normal.size() + emergency.size());

        double avg = 0;
        if (!selesai.isEmpty()) {
            avg = (double) totalWait / selesai.size();
        }

        System.out.printf("║  Rata-rata tunggu   : %-12.1f dtk║%n", avg);
        System.out.printf("║  Tunggu terlama     : %-15s║%n", longestPatient);

        int estimasi = (normal.size() + emergency.size()) * 10;
        System.out.printf("║  Estimasi antrian   : %-11d mnt║%n", estimasi);

        System.out.println("╚══════════════════════════════════════╝");
    }

    public void simpanRiwayat(Patient p, long wait) {
        try (FileWriter fw = new FileWriter("riwayat_pasien.txt", true)) {
            String waktu = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            fw.write("[" + waktu + "] "
                    + p.nama + " | "
                    + p.keluhan + " | "
                    + "urgency " + p.urgency + " | "
                    + "tunggu " + wait + " detik\n");
        } catch (IOException e) {
            System.out.println("⚠ Gagal menyimpan riwayat: " + e.getMessage());
        }
    }

    public static void lihatRiwayat() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║         RIWAYAT PASIEN               ║");
        System.out.println("╠══════════════════════════════════════╣");
        try (BufferedReader br = new BufferedReader(new FileReader("riwayat_pasien.txt"))) {
            String line;
            boolean ada = false;
            while ((line = br.readLine()) != null) {
                System.out.println("║  " + line);
                ada = true;
            }
            if (!ada) System.out.println("║  (belum ada riwayat)                 ║");
        } catch (IOException e) {
            System.out.println("║  (file belum ada / belum ada pasien  ║");
            System.out.println("║   yang dipanggil)                    ║");
        }
        System.out.println("╚══════════════════════════════════════╝");
    }
}