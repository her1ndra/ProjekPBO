package view;



import control.ControllerPuskesmas;
import model.ModelPuskesmas;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class TransaksiView extends JFrame {
    private JComboBox<String> cmbPasien, cmbDokter, cmbObat;
    private JFormattedTextField txtJumlah;
    private JSpinner dateSpinner;
    private JButton btnTambah, btnHapus, btnRefresh;
    private JTable tableTransaksi;
    private DefaultTableModel tableModel;
    private ControllerPuskesmas controller;
    private int selectedId = -1;

    public TransaksiView() {
        controller = new ControllerPuskesmas();
        initComponents();
        loadComboBoxData();
        refreshTable();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Transaksi Konsultasi - Sistem Puskesmas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLayout(new BorderLayout());

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
            loadComboBoxData();
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

    private void loadComboBoxData() {
        // Load Pasien
        cmbPasien.removeAllItems();
        List<ModelPuskesmas> listPasien = controller.getAllPasien();
        for (ModelPuskesmas pasien : listPasien) {
            cmbPasien.addItem(pasien.id_pasien + " - " + pasien.nama_pasien);
        }

        // Load Dokter
        cmbDokter.removeAllItems();
        List<ModelPuskesmas> listDokter = controller.getAllDokter();
        for (ModelPuskesmas dokter : listDokter) {
            cmbDokter.addItem(dokter.id_dokter + " - " + dokter.nama_dokter + " (" + dokter.spesialis + ")");
        }

        // Load Obat
        cmbObat.removeAllItems();
        List<ModelPuskesmas> listObat = controller.getAllObat();
        for (ModelPuskesmas obat : listObat) {
            cmbObat.addItem(obat.id_obat + " - " + obat.nama_obat + " (Rp " + obat.harga_obat + ")");
        }
    }

    private void tambahTransaksi() {
        if (validateInput()) {
            // Extract IDs from combo box selections
            String selectedPasien = (String) cmbPasien.getSelectedItem();
            String selectedDokter = (String) cmbDokter.getSelectedItem();
            String selectedObat = (String) cmbObat.getSelectedItem();
            
            int idPasien = Integer.parseInt(selectedPasien.split(" - ")[0]);
            int idDokter = Integer.parseInt(selectedDokter.split(" - ")[0]);
            int idObat = Integer.parseInt(selectedObat.split(" - ")[0]);
            
            int jumlah = ((Number) txtJumlah.getValue()).intValue();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String tglTransaksi = sdf.format(dateSpinner.getValue());
            
            controller.tambahTransaksi(idPasien, idDokter, idObat, jumlah, tglTransaksi);
            JOptionPane.showMessageDialog(this, "Transaksi berhasil ditambahkan!");
            clearForm();
            refreshTable();
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
        
        if (cmbObat.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Pilih obat!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        int jumlah = ((Number) txtJumlah.getValue()).intValue();
        if (jumlah <= 0) {
            JOptionPane.showMessageDialog(this, "Jumlah harus lebih dari 0!", "Error", JOptionPane.ERROR_MESSAGE);
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