package control;

import java.sql.*;
import java.util.ArrayList;

public class KoneksiDb {
    String DBurl = "jdbc:mysql://localhost/puskesmas";
    String DBusername= "root";
    String DBpassword= "";
    
    public Connection koneksi;
    public Statement statement;
    
    public KoneksiDb(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            koneksi = (Connection) DriverManager.getConnection(DBurl, DBusername, DBpassword);
            System.out.println("Koneksi Berhasil");
        }catch(Exception e){
            System.out.println("Koneksi Gagal");
        }
    }
    
    public Connection getKoneksi() {
        return koneksi;
    }
    
    public void insertTransaksi(int idPasien, int idDokter, Integer idObat, int jumlah) {
        String query;
        if (idObat != null) {
            query = "INSERT INTO transaksi (id_pasien, id_dokter, id_obat, jumlah, total_harga, tgl_transaksi) " +
                    "VALUES (?, ?, ?, ?, (SELECT harga_obat FROM obat WHERE id_obat = ?) * ?, CURRENT_DATE)";
        } else {
            query = "INSERT INTO transaksi (id_pasien, id_dokter, jumlah, total_harga, tgl_transaksi) " +
                    "VALUES (?, ?, ?, 0, CURRENT_DATE)";
        }

        try (PreparedStatement ps = koneksi.prepareStatement(query)) {
            ps.setInt(1, idPasien);
            ps.setInt(2, idDokter);

            if (idObat != null) {
                ps.setInt(3, idObat);
                ps.setInt(4, jumlah);
                ps.setInt(5, idObat);
                ps.setInt(6, jumlah);
            } else {
                ps.setInt(3, jumlah);
            }

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String[] getAllPasien() {
        ArrayList<String> list = new ArrayList<>();
        try {
            Statement st = koneksi.createStatement();
            ResultSet rs = st.executeQuery("SELECT id_pasien, nama_pasien, keluhan FROM pasien");
            while (rs.next()) {
                int id = rs.getInt("id_pasien");
                String nama = rs.getString("nama_pasien");
                String keluhan = rs.getString("keluhan");
                list.add(id + " - " + nama + " - " + keluhan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list.toArray(new String[0]);
    }

    public String[] getAllDokter() {
        ArrayList<String> list = new ArrayList<>();
        try {
            Statement st = koneksi.createStatement();
            ResultSet rs = st.executeQuery("SELECT id_dokter, nama_dokter, spesialis FROM dokter");
            while (rs.next()) {
                int id = rs.getInt("id_dokter");
                String nama = rs.getString("nama_dokter");
                String spesialis = rs.getString("spesialis");
                list.add(id + " - " + nama + " - " + spesialis);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list.toArray(new String[0]);
    }

    public String[] getAllObat() {
        ArrayList<String> list = new ArrayList<>();
        try {
            Statement st = koneksi.createStatement();
            ResultSet rs = st.executeQuery("SELECT id_obat, nama_obat FROM obat");
            while (rs.next()) {
                int id = rs.getInt("id_obat");
                String nama = rs.getString("nama_obat");
                list.add(id + " - " + nama);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list.toArray(new String[0]);
    }
}