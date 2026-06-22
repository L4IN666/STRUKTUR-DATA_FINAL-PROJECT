import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        HospitalQueue rs = new HospitalQueue();

        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║     SMART HOSPITAL QUEUE SYSTEM      ║");
        System.out.println("╚══════════════════════════════════════╝");

        int pilih = 0;

        while (pilih != 6) {

            System.out.println("\n──────────────────────────────────────");
            System.out.println("  MENU UTAMA");
            System.out.println("──────────────────────────────────────");
            System.out.println("  1. Tambah pasien");
            System.out.println("  2. Panggil pasien");
            System.out.println("  3. Lihat antrian");
            System.out.println("  4. Statistik");
            System.out.println("  5. Real-time monitor");
            System.out.println("  6. Exit");
            System.out.println("──────────────────────────────────────");
            System.out.print("  Pilih menu: ");

            if (!in.hasNextInt()) {
                System.out.println("  ⚠ Masukkan angka 1-6.");
                in.next();
                continue;
            }

            pilih = in.nextInt();
            in.nextLine();

            switch (pilih) {

                case 1:
                    System.out.println("\n── Tambah Pasien Baru ──");
                    System.out.print("  Nama      : ");
                    String nama = in.nextLine();

                    System.out.print("  Keluhan   : ");
                    String keluhan = in.nextLine();

                    int urg = 0;

                    while (true) {
                        System.out.print("  Urgency (1-5) [>=3 = emergency] : ");

                        if (in.hasNextInt()) {
                            urg = in.nextInt();
                            if (urg >= 1 && urg <= 5) {
                                break;
                            } else {
                                System.out.println("  ⚠ Urgency harus antara 1 sampai 5.");
                            }
                        } else {
                            System.out.println("  ⚠ Masukkan angka.");
                            in.next();
                        }
                    }

                    in.nextLine();

                    Patient p = new Patient(nama, keluhan, urg);
                    rs.tambahPasien(p);
                    break;

                case 2:
                    rs.panggilPasien();
                    break;

                case 3:
                    rs.lihatQueue();
                    break;

                case 4:
                    rs.statistik();
                    break;

                case 5:
                    rs.realTimeMonitor();
                    break;

                case 6:
                    System.out.println("\n✅ Program selesai. Sampai jumpa!");
                    break;

                default:
                    System.out.println("  ⚠ Menu tidak tersedia. Pilih 1-6.");
            }
        }

        in.close();
    }
}