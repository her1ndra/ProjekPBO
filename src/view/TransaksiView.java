package view;



import control.ControllerPuskesmas;
import model.ModelPuskesmas;
import control.KoneksiDb;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class TransaksiView extends JFrame {
    private JComboBox cmbPasien, cmbDokter, cmbObat;
    private JFormattedTextField txtJumlah;
    private JSpinner dateSpinner;
    private JButton btnTambah, btnHapus, btnRefresh;
    private JTable tableTransaksi;
    private DefaultTableModel tableModel;
    private ControllerPuskesmas controller;
    private int selectedId = -1;
    private JTextField txtId;
    
    KoneksiDb koneksi = new KoneksiDb();

    private void resetForm() {
        cmbPasien.setSelectedIndex(-1);
        cmbDokter.setSelectedIndex(-1);
        cmbObat.setSelectedIndex(-1);
        txtJumlah.setText("");
    }
    
    private void initComponents() {
        setTitle("Transaksi Konsultasi - Sistem Puskesmas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLayout(new BorderLayout());
        txtId = new JTextField();

        // Panel Input
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Input Transaksi"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Pasien
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Pasien:"), gbc);
        cmbPasien = new JComboBox<>();
        cmbPasien.setPreferredSize(new Dimension(200, 25));
        gbc.gridx = 1; gbc.gridy = 0;
        inputPanel.add(cmbPasien, gbc);

        // Dokter
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Dokter:"), gbc);
        cmbDokter = new JComboBox<>();
        cmbDokter.setPreferredSize(new Dimension(200, 25));
        gbc.gridx = 1; gbc.gridy = 1;
        inputPanel.add(cmbDokter, gbc);

        // Obat
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Obat:"), gbc);
        cmbObat = new JComboBox<>();
        cmbObat.setPreferredSize(new Dimension(200, 25));
        gbc.gridx = 1; gbc.gridy = 2;
        inputPanel.add(cmbObat, gbc);

        // Jumlah
        gbc.gridx = 0; gbc.gridy = 3;
        inputPanel.add(new JLabel("Jumlah:"), gbc);
        NumberFormat numberFormat = NumberFormat.getIntegerInstance();
        txtJumlah = new JFormattedTextField(numberFormat);
        txtJumlah.setColumns(10);
        txtJumlah.setValue(1);
        gbc.gridx = 1; gbc.gridy = 3;
        inputPanel.add(txtJumlah, gbc);

        // Tanggal Transaksi
        gbc.gridx = 0; gbc.gridy = 4;
        inputPanel.add(new JLabel("Tanggal Transaksi:"), gbc);
        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(editor);
        gbc.gridx = 1; gbc.gridy = 4;
        inputPanel.add(dateSpinner, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnTambah = new JButton("Tambah Transaksi");
        btnHapus = new JButton("Hapus");
        btnRefresh = new JButton("Refresh");

        buttonPanel.add(btnTambah);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnRefresh);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        inputPanel.add(buttonPanel, gbc);

        add(inputPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Pasien", "Dokter", "Obat", "Jumlah", "Total Harga", "Tanggal"};
        tableModel = new DefaultTableModel(columns, 0);
        tableTransaksi = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableTransaksi);
        add(scrollPane, BorderLayout.CENTER);

        // Event listeners
        btnTambah.addActionListener(e -> tambahTransaksi());
        btnHapus.addActionListener(e -> hapusTransaksi());
        btnRefresh.addActionListener(e -> {
            refreshTable();
        });

        tableTransaksi.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tableTransaksi.getSelectedRow();
                if (selectedRow >= 0) {
                    selectedId = (Integer) tableModel.getValueAt(selectedRow, 0);
                }
            }
        });
    }
    
    public TransaksiView() {
        controller = new ControllerPuskesmas();
        initComponents();
        refreshTable();
        loadDataToComboBox();
        setLocationRelativeTo(null);
        resetForm();
    }

    private void loadDataToComboBox() {
        cmbPasien.removeAllItems();
        for (String item : koneksi.getAllPasien()) {
            cmbPasien.addItem(item);
        }

        cmbDokter.removeAllItems();
        for (String item : koneksi.getAllDokter()) {
            cmbDokter.addItem(item);
        }

        cmbObat.removeAllItems();
        cmbObat.addItem("-");
        for (String item : koneksi.getAllObat()) {
            cmbObat.addItem(item);
        }
    }
 
    private void tambahTransaksi() {
        try {
            String pasienItem = (String) cmbPasien.getSelectedItem();
            String dokterItem = (String) cmbDokter.getSelectedItem();
            String obatItem = (String) cmbObat.getSelectedItem();
            String jumlah = txtJumlah.getText();

            int idPasien = Integer.parseInt(pasienItem.split(" - ")[0]);
            int idDokter = Integer.parseInt(dokterItem.split(" - ")[0]);
            Integer idObat = null;

            if (obatItem != null && !obatItem.equals("-")) {
                idObat = Integer.parseInt(obatItem.split(" - ")[0]);
            }

            koneksi.insertTransaksi(idPasien, idDokter, idObat, jumlah);
            JOptionPane.showMessageDialog(null, "Transaksi berhasil ditambahkan.");
            resetForm();
            refreshTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal menambahkan transaksi: " + e.getMessage());
        }
    }

    private void hapusTransaksi() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih transaksi yang akan dihapus!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin menghapus transaksi ini?", 
            "Konfirmasi Hapus", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            controller.hapusTransaksi(selectedId);
            JOptionPane.showMessageDialog(this, "Transaksi berhasil dihapus!");
            clearForm();
            refreshTable();
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<ModelPuskesmas> listTransaksi = controller.getAllTransaksi();
        
        for (ModelPuskesmas transaksi : listTransaksi) {
            Object[] row = {
                transaksi.id_transaksi,
                transaksi.nama_pasien,
                transaksi.nama_dokter,
                transaksi.nama_obat,
                transaksi.jumlah,
                "Rp " + String.format("%,d", transaksi.total_harga),
                transaksi.tgl_transaksi
            };
            tableModel.addRow(row);
        }
    }

    private boolean validateInput() {
        if (cmbPasien.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Pilih pasien!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (cmbDokter.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Pilih dokter!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }

    private void clearForm() {
        if (cmbPasien.getItemCount() > 0) cmbPasien.setSelectedIndex(0);
        if (cmbDokter.getItemCount() > 0) cmbDokter.setSelectedIndex(0);
        if (cmbObat.getItemCount() > 0) cmbObat.setSelectedIndex(0);
        txtJumlah.setValue(1);
        dateSpinner.setValue(new java.util.Date());
        selectedId = -1;
        tableTransaksi.clearSelection();
    }
}