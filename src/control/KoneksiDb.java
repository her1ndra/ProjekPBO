package control;

import java.sql.*;

public class KoneksiDb {
    String DBurl = "jdbc:mysql://localhost/puskesmas";
    String DBusername= "root";
    String DBpassword= "";
    
    Connection koneksi;
    Statement statement;
    
    public KoneksiDb(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            koneksi = (Connection) DriverManager.getConnection(DBurl, DBusername, DBpassword);
            System.out.println("Koneksi Berhasil");
        }catch(Exception e){
            System.out.println("Koneksi Gagal");
        }
    }
}