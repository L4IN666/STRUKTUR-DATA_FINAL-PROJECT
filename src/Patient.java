public class Patient {

    String nama;
    String keluhan;
    int urgency;
    long waktuMasuk;

    public Patient(String nama, String keluhan, int urgency) {
        this.nama = nama;
        this.keluhan = keluhan;
        this.urgency = urgency;
        waktuMasuk = System.currentTimeMillis();
    }

    public String toString() {
        return nama + " | " + keluhan + " | urgency " + urgency;
    }
}