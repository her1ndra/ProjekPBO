package model;

public class ModelPuskesmas {
    public int id_op;
    public String usm;
    public String pass;
    public int id_pasien;
    public String nama_pasien;
    public String keluhan;
    public String tgl_lahir;
    public String gender;
    public String tgl_keluhan;
    public int id_dokter;
    public String nama_dokter;
    public String spesialis;
    public int id_obat;
    public String nama_obat;
    public int harga_obat;
    public int id_transaksi;
    public int jumlah;
    public int total_harga;
    public String tgl_transaksi;
    
    public ModelPuskesmas() {}
    
    public ModelPuskesmas(
        int id_op, String usm, String pass,
        int id_pasien, String nama_pasien, String keluhan, String tgl_lahir, String gender, String tgl_keluhan,
        int id_dokter, String nama_dokter, String spesialis,
        int id_obat, String nama_obat, int harga_obat,
        int id_transaksi, int jumlah, int total_harga, String tgl_transaksi
    ){
        this.id_op = id_op;
        this.usm = usm;
        this.pass = pass;

        this.id_pasien = id_pasien;
        this.nama_pasien = nama_pasien;
        this.keluhan = keluhan;
        this.tgl_lahir = tgl_lahir;
        this.gender = gender;
        this.tgl_keluhan = tgl_keluhan;

        this.id_dokter = id_dokter;
        this.nama_dokter = nama_dokter;
        this.spesialis = spesialis;

        this.id_obat = id_obat;
        this.nama_obat = nama_obat;
        this.harga_obat = harga_obat;

        this.id_transaksi = id_transaksi;
        this.jumlah = jumlah;
        this.total_harga = total_harga;
        this.tgl_transaksi = tgl_transaksi;
    }    
}
