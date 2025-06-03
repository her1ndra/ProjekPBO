package control;

import model.ModelPuskesmas;
import view.LoginView;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class ControllerPuskesmas {
    private KoneksiDb koneksi;
    private LoginView loginView;

    public ControllerPuskesmas(LoginView view) {
        this.loginView = view;
        this.koneksi = new KoneksiDb();
    }

    public ControllerPuskesmas() {
        this.koneksi = new KoneksiDb();
    }

    // Login operator
    public boolean login(String username, String password) {
        try {
            String query = "SELECT * FROM operator WHERE usm = ? AND pass = ?";
            PreparedStatement ps = koneksi.koneksi.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // CRUD Pasien
    public void tambahPasien(String nama, String keluhan, String tglLahir, String gender, String tglKeluhan) {
        try {
            String query = "INSERT INTO pasien (nama, keluhan, tgl_lahir, gender, tgl_keluhan) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = koneksi.koneksi.prepareStatement(query);
            ps.setString(1, nama);
            ps.setString(2, keluhan);
            ps.setString(3, tglLahir);
            ps.setString(4, gender);
            ps.setString(5, tglKeluhan);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ModelPuskesmas> getAllPasien() {
        List<ModelPuskesmas> listPasien = new ArrayList<>();
        try {
            String query = "SELECT * FROM pasien";
            Statement stmt = koneksi.koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                ModelPuskesmas pasien = new ModelPuskesmas();
                pasien.id_pasien = rs.getInt("id_pasien");
                pasien.nama_pasien = rs.getString("nama");
                pasien.keluhan = rs.getString("keluhan");
                pasien.tgl_lahir = rs.getString("tgl_lahir");
                pasien.gender = rs.getString("gender");
                pasien.tgl_keluhan = rs.getString("tgl_keluhan");
                listPasien.add(pasien);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listPasien;
    }

    public void updatePasien(int id, String nama, String keluhan, String tglLahir, String gender, String tglKeluhan) {
        try {
            String query = "UPDATE pasien SET nama=?, keluhan=?, tgl_lahir=?, gender=?, tgl_keluhan=? WHERE id_pasien=?";
            PreparedStatement ps = koneksi.koneksi.prepareStatement(query);
            ps.setString(1, nama);
            ps.setString(2, keluhan);
            ps.setString(3, tglLahir);
            ps.setString(4, gender);
            ps.setString(5, tglKeluhan);
            ps.setInt(6, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hapusPasien(int id) {
        try {
            String query = "DELETE FROM pasien WHERE id_pasien = ?";
            PreparedStatement ps = koneksi.koneksi.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // CRUD Dokter
    public void tambahDokter(String nama, String spesialis) {
        try {
            String query = "INSERT INTO dokter (nama_dokter, spesialis) VALUES (?, ?)";
            PreparedStatement ps = koneksi.koneksi.prepareStatement(query);
            ps.setString(1, nama);
            ps.setString(2, spesialis);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ModelPuskesmas> getAllDokter() {
        List<ModelPuskesmas> listDokter = new ArrayList<>();
        try {
            String query = "SELECT * FROM dokter";
            Statement stmt = koneksi.koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                ModelPuskesmas dokter = new ModelPuskesmas();
                dokter.id_dokter = rs.getInt("id_dokter");
                dokter.nama_dokter = rs.getString("nama_dokter");
                dokter.spesialis = rs.getString("spesialis");
                listDokter.add(dokter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listDokter;
    }

    public void updateDokter(int id, String nama, String spesialis) {
        try {
            String query = "UPDATE dokter SET nama_dokter=?, spesialis=? WHERE id_dokter=?";
            PreparedStatement ps = koneksi.koneksi.prepareStatement(query);
            ps.setString(1, nama);
            ps.setString(2, spesialis);
            ps.setInt(3, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hapusDokter(int id) {
        try {
            String query = "DELETE FROM dokter WHERE id_dokter = ?";
            PreparedStatement ps = koneksi.koneksi.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // CRUD Obat
    public void tambahObat(String nama, int harga) {
        try {
            String query = "INSERT INTO obat (nama_obat, harga_obat) VALUES (?, ?)";
            PreparedStatement ps = koneksi.koneksi.prepareStatement(query);
            ps.setString(1, nama);
            ps.setInt(2, harga);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ModelPuskesmas> getAllObat() {
        List<ModelPuskesmas> listObat = new ArrayList<>();
        try {
            String query = "SELECT * FROM obat";
            Statement stmt = koneksi.koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                ModelPuskesmas obat = new ModelPuskesmas();
                obat.id_obat = rs.getInt("id_obat");
                obat.nama_obat = rs.getString("nama_obat");
                obat.harga_obat = rs.getInt("harga_obat");
                listObat.add(obat);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listObat;
    }

    public void updateObat(int id, String nama, int harga) {
        try {
            String query = "UPDATE obat SET nama_obat=?, harga_obat=? WHERE id_obat=?";
            PreparedStatement ps = koneksi.koneksi.prepareStatement(query);
            ps.setString(1, nama);
            ps.setInt(2, harga);
            ps.setInt(3, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hapusObat(int id) {
        try {
            String query = "DELETE FROM obat WHERE id_obat = ?";
            PreparedStatement ps = koneksi.koneksi.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // CRUD Transaksi
    public void tambahTransaksi(int idPasien, int idDokter, int idObat, int jumlah, String tglTransaksi) {
        try {
            // Ambil harga obat
            String queryHarga = "SELECT harga_obat FROM obat WHERE id_obat = ?";
            PreparedStatement psHarga = koneksi.koneksi.prepareStatement(queryHarga);
            psHarga.setInt(1, idObat);
            ResultSet rs = psHarga.executeQuery();
            
            int hargaObat = 0;
            if (rs.next()) {
                hargaObat = rs.getInt("harga_obat");
            }
            
            int totalHarga = hargaObat * jumlah;
            
            String query = "INSERT INTO transaksi (id_pasien, id_dokter, id_obat, jumlah, total_harga, tgl_transaksi) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = koneksi.koneksi.prepareStatement(query);
            ps.setInt(1, idPasien);
            ps.setInt(2, idDokter);
            ps.setInt(3, idObat);
            ps.setInt(4, jumlah);
            ps.setInt(5, totalHarga);
            ps.setString(6, tglTransaksi);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ModelPuskesmas> getAllTransaksi() {
        List<ModelPuskesmas> listTransaksi = new ArrayList<>();
        try {
            String query = "SELECT t.*, p.nama as nama_pasien, d.nama_dokter, o.nama_obat " +
                          "FROM transaksi t " +
                          "JOIN pasien p ON t.id_pasien = p.id_pasien " +
                          "JOIN dokter d ON t.id_dokter = d.id_dokter " +
                          "JOIN obat o ON t.id_obat = o.id_obat";
            Statement stmt = koneksi.koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                ModelPuskesmas transaksi = new ModelPuskesmas();
                transaksi.id_transaksi = rs.getInt("id_transaksi");
                transaksi.id_pasien = rs.getInt("id_pasien");
                transaksi.nama_pasien = rs.getString("nama_pasien");
                transaksi.id_dokter = rs.getInt("id_dokter");
                transaksi.nama_dokter = rs.getString("nama_dokter");
                transaksi.id_obat = rs.getInt("id_obat");
                transaksi.nama_obat = rs.getString("nama_obat");
                transaksi.jumlah = rs.getInt("jumlah");
                transaksi.total_harga = rs.getInt("total_harga");
                transaksi.tgl_transaksi = rs.getString("tgl_transaksi");
                listTransaksi.add(transaksi);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listTransaksi;
    }

    public void hapusTransaksi(int id) {
        try {
            String query = "DELETE FROM transaksi WHERE id_transaksi = ?";
            PreparedStatement ps = koneksi.koneksi.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}