import java.util.*;

public class HospitalQueue {

    Queue<Patient> normal = new LinkedList<>();

    PriorityQueue<Patient> emergency =
            new PriorityQueue<>(
                    (a,b)->{

                        if(a.urgency != b.urgency){
                            return b.urgency - a.urgency;
                        }

                        return Long.compare(
                                a.waktuMasuk,
                                b.waktuMasuk
                        );
                    }
            );

    ArrayList<Patient> selesai =
            new ArrayList<>();

    long totalWait = 0;
    long longestWait = 0;
    String longestPatient = "none";

    public void tambahPasien(Patient p){

        if(p.urgency >= 3){

            emergency.add(p);
            System.out.println(
                    "pasien emergency masuk"
            );

        }else{

            normal.add(p);
            System.out.println(
                    "pasien normal masuk"
            );
        }
    }

    public void panggilPasien(){

        Patient p = null;

        if(!emergency.isEmpty()){

            p = emergency.poll();

        }else if(!normal.isEmpty()){

            p = normal.poll();
        }

        if(p == null){

            System.out.println(
                    "ga ada pasien"
            );
            return;
        }

        long wait =
                (System.currentTimeMillis()
                        - p.waktuMasuk)
                        /1000;

        totalWait += wait;

        if(wait > longestWait){

            longestWait = wait;
            longestPatient = p.nama;
        }

        selesai.add(p);

        System.out.println(
                "\nDipanggil : "
        );

        System.out.println(p);

        System.out.println(
                "waiting "
                        + wait
                        + " detik"
        );
    }

    public void lihatQueue(){

        System.out.println(
                "\n=== emergency queue ==="
        );

        if(emergency.isEmpty()){
            System.out.println("kosong");
        }

        for(Patient p : emergency){
            System.out.println(p);
        }

        System.out.println(
                "\n=== normal queue ==="
        );

        if(normal.isEmpty()){
            System.out.println("kosong");
        }

        for(Patient p : normal){
            System.out.println(p);
        }
    }

    public void statistik(){

        System.out.println(
                "\n=== statistik ==="
        );

        int total =
                normal.size()
                        + emergency.size()
                        + selesai.size();

        System.out.println(
                "total pasien : "
                        + total
        );

        System.out.println(
                "processed : "
                        + selesai.size()
        );

        double avg = 0;

        if(selesai.size() > 0){

            avg =
                    (double) totalWait
                            / selesai.size();
        }

        System.out.println(
                "avg waiting : "
                        + avg
                        + " detik"
        );

        System.out.println(
                "longest waiting : "
                        + longestPatient
        );

        int estimasi =
                (normal.size()
                        + emergency.size())
                        *10;

        System.out.println(
                "estimasi queue : "
                        + estimasi
                        + " menit"
        );
    }
}