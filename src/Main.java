import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        HospitalQueue rs = new HospitalQueue();

        int pilih = 0;

        while (pilih != 5) {

            System.out.println("\nSMART HOSPITAL QUEUE");
            System.out.println("1 tambah pasien");
            System.out.println("2 panggil pasien");
            System.out.println("3 lihat queue");
            System.out.println("4 statistik");
            System.out.println("5 exit");
            System.out.print("pilih : ");

            if(!in.hasNextInt()){
                System.out.println("input angka");
                in.next();
                continue;
            }

            pilih = in.nextInt();
            in.nextLine();

            switch (pilih){

                case 1:

                    System.out.print("nama : ");
                    String nama = in.nextLine();

                    System.out.print("keluhan : ");
                    String keluhan = in.nextLine();

                    int urg;

                    while(true){

                        System.out.print("urgency 1-5 : ");

                        if(in.hasNextInt()){
                            urg = in.nextInt();

                            if(urg >=1 && urg <=5){
                                break;
                            }
                        }else{
                            in.next();
                        }

                        System.out.println("urgency harus 1-5");
                    }

                    in.nextLine();

                    Patient p = new Patient(
                            nama,
                            keluhan,
                            urg
                    );

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
                    System.out.println("program selesai");
                    break;

                default:
                    System.out.println("menu ga ada");
            }
        }

        in.close();
    }
}