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
    
    public void insertTransaksi(Integer idPasien, Integer idDokter, Integer idObat, String jumlah) {
        try {
            String sql = "INSERT INTO transaksi (id_pasien, id_dokter, id_obat, jumlah, tgl_transaksi) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = koneksi.prepareStatement(sql);

            ps.setInt(1, idPasien);
            ps.setInt(2, idDokter);

            if (idObat == null) {
                ps.setNull(3, Types.INTEGER);
            } else {
                ps.setInt(3, idObat);
            }

            if (jumlah == null || jumlah.isEmpty()) {
                ps.setNull(4, Types.VARCHAR);
            } else {
                ps.setString(4, jumlah);
            }
            
            Timestamp now = new Timestamp(System.currentTimeMillis());
            ps.setTimestamp(5, now);

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