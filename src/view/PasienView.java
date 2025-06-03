package view;

import control.ControllerPuskesmas;
import model.ModelPuskesmas;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;

public class PasienView extends JFrame {
    private JTextField txtNama, txtKeluhan;
    private JSpinner dateSpinnerLahir, dateSpinnerKeluhan;
    private JComboBox<String> cmbGender;
    private JButton btnTambah, btnUpdate, btnHapus, btnRefresh;
    private JTable tablePasien;
    private DefaultTableModel tableModel;
    private ControllerPuskesmas controller;
    private int selectedId = -1;

    public PasienView() {
        controller = new ControllerPuskesmas();
        initComponents();
        refreshTable();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Data Pasien - Sistem Puskesmas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Panel Input
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Input Data Pasien"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Nama
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Nama:"), gbc);
        txtNama = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 0;
        inputPanel.add(txtNama, gbc);

        // Keluhan
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Keluhan:"), gbc);
        txtKeluhan = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 1;
        inputPanel.add(txtKeluhan, gbc);

        // Tanggal Lahir
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Tanggal Lahir:"), gbc);
        dateSpinnerLahir = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorLahir = new JSpinner.DateEditor(dateSpinnerLahir, "yyyy-MM-dd");
        dateSpinnerLahir.setEditor(editorLahir);
        gbc.gridx = 1; gbc.gridy = 2;
        inputPanel.add(dateSpinnerLahir, gbc);

        // Gender
        gbc.gridx = 0; gbc.gridy = 3;
        inputPanel.add(new JLabel("Gender:"), gbc);
        cmbGender = new JComboBox<>(new String[]{"L", "P"});
        gbc.gridx = 1; gbc.gridy = 3;
        inputPanel.add(cmbGender, gbc);

        // Tanggal Keluhan
        gbc.gridx = 0; gbc.gridy = 4;
        inputPanel.add(new JLabel("Tanggal Keluhan:"), gbc);
        dateSpinnerKeluhan = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorKeluhan = new JSpinner.DateEditor(dateSpinnerKeluhan, "yyyy-MM-dd");
        dateSpinnerKeluhan.setEditor(editorKeluhan);
        gbc.gridx = 1; gbc.gridy = 4;
        inputPanel.add(dateSpinnerKeluhan, gbc);

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

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        inputPanel.add(buttonPanel, gbc);

        add(inputPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Nama", "Keluhan", "Tanggal Lahir", "Gender", "Tanggal Keluhan"};
        tableModel = new DefaultTableModel(columns, 0);
        tablePasien = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tablePasien);
        add(scrollPane, BorderLayout.CENTER);

        // Event listeners
        btnTambah.addActionListener(e -> tambahPasien());
        btnUpdate.addActionListener(e -> updatePasien());
        btnHapus.addActionListener(e -> hapusPasien());
        btnRefresh.addActionListener(e -> refreshTable());

        tablePasien.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tablePasien.getSelectedRow();
                if (selectedRow >= 0) {
                    selectedId = (Integer) tableModel.getValueAt(selectedRow, 0);
                    txtNama.setText((String) tableModel.getValueAt(selectedRow, 1));
                    txtKeluhan.setText((String) tableModel.getValueAt(selectedRow, 2));
                    
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        dateSpinnerLahir.setValue(sdf.parse((String) tableModel.getValueAt(selectedRow, 3)));
                        dateSpinnerKeluhan.setValue(sdf.parse((String) tableModel.getValueAt(selectedRow, 5)));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    
                    cmbGender.setSelectedItem(tableModel.getValueAt(selectedRow, 4));
                }
            }
        });
    }

    private void tambahPasien() {
        if (validateInput()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String tglLahir = sdf.format(dateSpinnerLahir.getValue());
            String tglKeluhan = sdf.format(dateSpinnerKeluhan.getValue());
            
            controller.tambahPasien(
                txtNama.getText(),
                txtKeluhan.getText(),
                tglLahir,
                cmbGender.getSelectedItem().toString(),
                tglKeluhan
            );
            
            JOptionPane.showMessageDialog(this, "Data pasien berhasil ditambahkan!");
            clearForm();
            refreshTable();
        }
    }

    private void updatePasien() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan diupdate!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (validateInput()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String tglLahir = sdf.format(dateSpinnerLahir.getValue());
            String tglKeluhan = sdf.format(dateSpinnerKeluhan.getValue());
            
            controller.updatePasien(
                selectedId,
                txtNama.getText(),
                txtKeluhan.getText(),
                tglLahir,
                cmbGender.getSelectedItem().toString(),
                tglKeluhan
            );
            
            JOptionPane.showMessageDialog(this, "Data pasien berhasil diupdate!");
            clearForm();
            refreshTable();
        }
    }

    private void hapusPasien() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan dihapus!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin menghapus data ini?", 
            "Konfirmasi Hapus", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            controller.hapusPasien(selectedId);
            JOptionPane.showMessageDialog(this, "Data pasien berhasil dihapus!");
            clearForm();
            refreshTable();
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<ModelPuskesmas> listPasien = controller.getAllPasien();
        
        for (ModelPuskesmas pasien : listPasien) {
            Object[] row = {
                pasien.id_pasien,
                pasien.nama_pasien,
                pasien.keluhan,
                pasien.tgl_lahir,
                pasien.gender,
                pasien.tgl_keluhan
            };
            tableModel.addRow(row);
        }
    }

    private boolean validateInput() {
        if (txtNama.getText().trim().isEmpty() || txtKeluhan.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void clearForm() {
        txtNama.setText("");
        txtKeluhan.setText("");
        dateSpinnerLahir.setValue(new java.util.Date());
        dateSpinnerKeluhan.setValue(new java.util.Date());
        cmbGender.setSelectedIndex(0);
        selectedId = -1;
        tablePasien.clearSelection();
    }
}