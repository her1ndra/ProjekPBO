package view;

import control.ControllerPuskesmas;
import model.ModelPuskesmas;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.List;

public class ObatView extends JFrame {
    private JTextField txtNama;
    private JFormattedTextField txtHarga;
    private JButton btnTambah, btnUpdate, btnHapus, btnRefresh;
    private JTable tableObat;
    private DefaultTableModel tableModel;
    private ControllerPuskesmas controller;
    private int selectedId = -1;

    public ObatView() {
        controller = new ControllerPuskesmas();
        initComponents();
        refreshTable();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Data Obat - Sistem Puskesmas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setLayout(new BorderLayout());

        // Panel Input
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Input Data Obat"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Nama Obat
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Nama Obat:"), gbc);
        txtNama = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 0;
        inputPanel.add(txtNama, gbc);

        // Harga Obat
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Harga Obat:"), gbc);
        NumberFormat numberFormat = NumberFormat.getIntegerInstance();
        txtHarga = new JFormattedTextField(numberFormat);
        txtHarga.setColumns(20);
        txtHarga.setValue(0);
        gbc.gridx = 1; gbc.gridy = 1;
        inputPanel.add(txtHarga, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnTambah = new JButton("Tambah");
        btnUpdate = new JButton("Update");
        btnHapus = new JButton("Hapus");
        btnRefresh = new JButton("Refresh");

        buttonPanel.add(btnTambah);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnRefresh);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        inputPanel.add(buttonPanel, gbc);

        add(inputPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Nama Obat", "Harga Obat"};
        tableModel = new DefaultTableModel(columns, 0);
        tableObat = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableObat);
        add(scrollPane, BorderLayout.CENTER);

        // Event listeners
        btnTambah.addActionListener(e -> tambahObat());
        btnUpdate.addActionListener(e -> updateObat());
        btnHapus.addActionListener(e -> hapusObat());
        btnRefresh.addActionListener(e -> refreshTable());

        tableObat.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tableObat.getSelectedRow();
                if (selectedRow >= 0) {
                    selectedId = (Integer) tableModel.getValueAt(selectedRow, 0);
                    txtNama.setText((String) tableModel.getValueAt(selectedRow, 1));
                    txtHarga.setValue((Integer) tableModel.getValueAt(selectedRow, 2));
                }
            }
        });
    }

    private void tambahObat() {
        if (validateInput()) {
            int harga = ((Number) txtHarga.getValue()).intValue();
            controller.tambahObat(txtNama.getText(), harga);
            JOptionPane.showMessageDialog(this, "Data obat berhasil ditambahkan!");
            clearForm();
            refreshTable();
        }
    }

    private void updateObat() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan diupdate!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (validateInput()) {
            int harga = ((Number) txtHarga.getValue()).intValue();
            controller.updateObat(selectedId, txtNama.getText(), harga);
            JOptionPane.showMessageDialog(this, "Data obat berhasil diupdate!");
            clearForm();
            refreshTable();
        }
    }

    private void hapusObat() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan dihapus!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin menghapus data ini?", 
            "Konfirmasi Hapus", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            controller.hapusObat(selectedId);
            JOptionPane.showMessageDialog(this, "Data obat berhasil dihapus!");
            clearForm();
            refreshTable();
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<ModelPuskesmas> listObat = controller.getAllObat();
        
        for (ModelPuskesmas obat : listObat) {
            Object[] row = {
                obat.id_obat,
                obat.nama_obat,
                obat.harga_obat
            };
            tableModel.addRow(row);
        }
    }

    private boolean validateInput() {
        if (txtNama.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama obat harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        int harga = ((Number) txtHarga.getValue()).intValue();
        if (harga <= 0) {
            JOptionPane.showMessageDialog(this, "Harga obat harus lebih dari 0!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }

    private void clearForm() {
        txtNama.setText("");
        txtHarga.setValue(0);
        selectedId = -1;
        tableObat.clearSelection();
    }
}