package view;

import control.ControllerPuskesmas;
import model.ModelPuskesmas;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class DokterView extends JFrame {
    private JTextField txtNama, txtSpesialis;
    private JButton btnTambah, btnUpdate, btnHapus, btnRefresh;
    private JTable tableDokter;
    private DefaultTableModel tableModel;
    private ControllerPuskesmas controller;
    private int selectedId = -1;

    public DokterView() {
        controller = new ControllerPuskesmas();
        initComponents();
        refreshTable();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Data Dokter - Sistem Puskesmas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setLayout(new BorderLayout());

        // Panel Input
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Input Data Dokter"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Nama Dokter
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Nama Dokter:"), gbc);
        txtNama = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 0;
        inputPanel.add(txtNama, gbc);

        // Spesialis
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Spesialis:"), gbc);
        txtSpesialis = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 1;
        inputPanel.add(txtSpesialis, gbc);

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
        String[] columns = {"ID", "Nama Dokter", "Spesialis"};
        tableModel = new DefaultTableModel(columns, 0);
        tableDokter = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableDokter);
        add(scrollPane, BorderLayout.CENTER);

        // Event listeners
        btnTambah.addActionListener(e -> tambahDokter());
        btnUpdate.addActionListener(e -> updateDokter());
        btnHapus.addActionListener(e -> hapusDokter());
        btnRefresh.addActionListener(e -> refreshTable());

        tableDokter.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tableDokter.getSelectedRow();
                if (selectedRow >= 0) {
                    selectedId = (Integer) tableModel.getValueAt(selectedRow, 0);
                    txtNama.setText((String) tableModel.getValueAt(selectedRow, 1));
                    txtSpesialis.setText((String) tableModel.getValueAt(selectedRow, 2));
                }
            }
        });
    }

    private void tambahDokter() {
        if (validateInput()) {
            controller.tambahDokter(txtNama.getText(), txtSpesialis.getText());
            JOptionPane.showMessageDialog(this, "Data dokter berhasil ditambahkan!");
            clearForm();
            refreshTable();
        }
    }

    private void updateDokter() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan diupdate!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (validateInput()) {
            controller.updateDokter(selectedId, txtNama.getText(), txtSpesialis.getText());
            JOptionPane.showMessageDialog(this, "Data dokter berhasil diupdate!");
            clearForm();
            refreshTable();
        }
    }

    private void hapusDokter() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan dihapus!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin menghapus data ini?", 
            "Konfirmasi Hapus", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            controller.hapusDokter(selectedId);
            JOptionPane.showMessageDialog(this, "Data dokter berhasil dihapus!");
            clearForm();
            refreshTable();
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<ModelPuskesmas> listDokter = controller.getAllDokter();
        
        for (ModelPuskesmas dokter : listDokter) {
            Object[] row = {
                dokter.id_dokter,
                dokter.nama_dokter,
                dokter.spesialis
            };
            tableModel.addRow(row);
        }
    }

    private boolean validateInput() {
        if (txtNama.getText().trim().isEmpty() || txtSpesialis.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void clearForm() {
        txtNama.setText("");
        txtSpesialis.setText("");
        selectedId = -1;
        tableDokter.clearSelection();
    }
}