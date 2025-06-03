package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuView extends JFrame {
    
    public MainMenuView() {
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Menu Utama - Sistem Puskesmas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLayout(new BorderLayout());

        // Panel title
        JPanel titlePanel = new JPanel();
        JLabel lblTitle = new JLabel("SISTEM INFORMASI PUSKESMAS");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(lblTitle);
        add(titlePanel, BorderLayout.NORTH);

        // Panel menu
        JPanel menuPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JButton btnPasien = new JButton("Data Pasien");
        JButton btnDokter = new JButton("Data Dokter");
        JButton btnObat = new JButton("Data Obat");
        JButton btnTransaksi = new JButton("Transaksi Konsultasi");
        JButton btnLogout = new JButton("Logout");

        // Styling buttons
        Dimension buttonSize = new Dimension(300, 50);
        Font buttonFont = new Font("Arial", Font.PLAIN, 14);
        
        btnPasien.setPreferredSize(buttonSize);
        btnPasien.setFont(buttonFont);
        btnDokter.setPreferredSize(buttonSize);
        btnDokter.setFont(buttonFont);
        btnObat.setPreferredSize(buttonSize);
        btnObat.setFont(buttonFont);
        btnTransaksi.setPreferredSize(buttonSize);
        btnTransaksi.setFont(buttonFont);
        btnLogout.setPreferredSize(buttonSize);
        btnLogout.setFont(buttonFont);

        menuPanel.add(btnPasien);
        menuPanel.add(btnDokter);
        menuPanel.add(btnObat);
        menuPanel.add(btnTransaksi);
        menuPanel.add(btnLogout);

        add(menuPanel, BorderLayout.CENTER);

        // Action listeners
        btnPasien.addActionListener(e -> {
            new PasienView().setVisible(true);
        });

        btnDokter.addActionListener(e -> {
            new DokterView().setVisible(true);
        });

        btnObat.addActionListener(e -> {
            new ObatView().setVisible(true);
        });

        btnTransaksi.addActionListener(e -> {
            new TransaksiView().setVisible(true);
        });

        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Apakah Anda yakin ingin logout?", 
                "Konfirmasi Logout", 
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginView().setVisible(true);
            }
        });
    }
}