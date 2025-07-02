/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form;

import popup.popup_pelanggan;
import popup.popup_layanan;
import popup.popup_antar;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import koneksi.koneksi;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.awt.event.FocusAdapter; 
import java.awt.event.FocusEvent;   
import java.awt.Color;               
import javax.swing.JTextField;   
/**
 *
 * @author USER
 */
public class cucian_masuk extends javax.swing.JFrame {
    public String id, nama, jk, nomor, member, almt;
    public String id_lay, jenis_lay, estimasi_waktu, hargaperkg;
    public String kode_ant, tipeantar, biayatambahan;
    private Connection conn = new koneksi().connect();
    private DefaultTableModel tabmode;
    
    private static final String PLACEHOLDER_berat = "Masukkan jumlah berat cucian (Kg)";

    /**
     * Creates new form cucian_masuk
     */
    public cucian_masuk() {
        initComponents();
        setLocationRelativeTo(this);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        aktif();
        kosong();
        tampilkanIdAdmin();
        String admin = UserID.getNamaAdmin();
        label_namaAdmin.setText(admin);
//        tampilkanNamaAdmin();
        almt_pelanggan.setLineWrap(true);
        almt_pelanggan.setWrapStyleWord(true);
        autonumber();
        
        addPlaceholder(berat, PLACEHOLDER_berat);
    }
    
    private void addPlaceholder(JTextField textField, String placeholderText) {
        textField.setText(placeholderText);
        textField.setForeground(Color.BLACK);

        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholderText)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholderText);
                    textField.setForeground(Color.BLACK);
                }
            }
        });
    }

    protected void aktif() {
        jtglnota.requestFocus();
        jtglnota.setFormats(new SimpleDateFormat("yyyy/MM/dd"));
        jtglnota.setDate(new Date());
        
        Object[] Baris = {"ID Layanan", "Layanan", "Estimasi Waktu", "Harga per Kg (Rp)", "Tipe", "Biaya Tambahan (Rp)", "Berat (Kg)", "Sub Total (Rp)"};
        tabmode = new DefaultTableModel(null, Baris);
        tbl_transaksi.setModel(tabmode);
        
        id_nota.setEditable(false);
        id_pelanggan.setEditable(false);
        nama_pelanggan.setEditable(false);
        nomor_pelanggan.setEditable(false);
        member_pelanggan.setEditable(false);
        almt_pelanggan.setEditable(false);
        id_layanan.setEditable(false);
        jenis_layanan.setEditable(false);
        estimasi.setEditable(false);
        harga_perkg.setEditable(false);
        kode_antar.setEditable(false);
        tipe_antar.setEditable(false);
        biaya_tambahan.setEditable(false);
        total_biaya.setEditable(false);
    }
    
    protected void kosong() {
        id_pelanggan.setEditable(false);
        nama_pelanggan.setEditable(false);
        nomor_pelanggan.setEditable(false);
        member_pelanggan.setEditable(false);
        almt_pelanggan.setEditable(false);
        id_layanan.setEditable(false);
        jenis_layanan.setEditable(false);
        estimasi.setEditable(false);
        harga_perkg.setEditable(false);
        kode_antar.setEditable(false);
        tipe_antar.setEditable(false);
        biaya_tambahan.setEditable(false);
        total_biaya.setEditable(false);
    }

    private void tampilkanIdAdmin() {
        String idAdmin = UserID.getIDAdmin();
        label_idAdmin.setText(idAdmin);
    }
    
    public void itemTerpilihPlgn() {
        popup_pelanggan pp = new popup_pelanggan();
        pp.plgn = this;
        id_pelanggan.setText(id);
        nama_pelanggan.setText(nama);
        nomor_pelanggan.setText(nomor);
        member_pelanggan.setText(member);
        almt_pelanggan.setText(almt);
    }

    public void itemTerpilihLayanan() {
        popup_layanan pl = new popup_layanan();
        pl.layanan = this;
        id_layanan.setText(id_lay);
        jenis_layanan.setText(jenis_lay);
        estimasi.setText(estimasi_waktu);
        harga_perkg.setText(hargaperkg);
    }

    public void itemTerpilihAntar() {
        popup_antar pa = new popup_antar();
        pa.antar = this;
        kode_antar.setText(kode_ant);
        tipe_antar.setText(tipeantar);
        biaya_tambahan.setText(biayatambahan);
    }

    protected void autonumber() {
        try {
            String sql = "SELECT id_nota from tb_nota order by id_nota asc";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            id_nota.setText("IN01");
            while (rs.next()) {
                String idnota = rs.getString("id_nota").substring(2);
                int AN = Integer.parseInt(idnota) + 1;
                String Nol = "";
                if (AN < 10) {
                    Nol = "0";
                }
                id_nota.setText("IN" + Nol + AN);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Auto Number Gagal" + e);
        }
    }
    
    public void hitung() {
        int total = 0;
        for (int i = 0; i < tbl_transaksi.getRowCount(); i++) {
            int amount = Integer.valueOf(tbl_transaksi.getValueAt(i, 7).toString());
            total += amount;
        }
        total_biaya.setText(Integer.toString(total));
    }
    
    public void cetak() {
        try{
            String loginId = UserID.getIDAdmin();
            String loginAdmin = "Tidak Diketahui";
            
            try (PreparedStatement admnama = conn.prepareStatement("SELECT nama FROM tb_login WHERE id_admin = ?")) {
                admnama.setString(1, loginId);
                try (ResultSet rsNama = admnama.executeQuery()) {
                    if (rsNama.next()) {
                        loginAdmin = rsNama.getString("nama");
                    }
                }
            }
            
            String path="./src/ireport/report_cucian.jasper";
            HashMap parameter = new HashMap();
            parameter.put("id_nota",id_nota.getText());
            
            JasperPrint print = JasperFillManager.fillReport(path,parameter,conn);
            JasperViewer viewer = new JasperViewer(print, false);
            viewer.setAlwaysOnTop(true);    // WAJIB agar tampil di atas form
            viewer.setVisible(true);
            viewer.toFront();
        }catch(Exception ex){
            JOptionPane.showMessageDialog(rootPane,"Dokumen Tidak Ada" +ex);   
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        panel_pelanggan = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        id_pelanggan = new javax.swing.JTextField();
        nama_pelanggan = new javax.swing.JTextField();
        nomor_pelanggan = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        almt_pelanggan = new javax.swing.JTextArea();
        bcari_pelanggan = new javax.swing.JButton();
        jLabel53 = new javax.swing.JLabel();
        member_pelanggan = new javax.swing.JTextField();
        detail_service = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        id_layanan = new javax.swing.JTextField();
        bcari_layanan = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jenis_layanan = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        estimasi = new javax.swing.JTextField();
        btambah_layanan = new javax.swing.JButton();
        jLabel51 = new javax.swing.JLabel();
        subTotal = new javax.swing.JTextField();
        harga_perkg = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        kode_antar = new javax.swing.JTextField();
        bcari_antar = new javax.swing.JButton();
        jLabel34 = new javax.swing.JLabel();
        tipe_antar = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        biaya_tambahan = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        berat = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_transaksi = new javax.swing.JTable();
        bhapus_transaksi = new javax.swing.JButton();
        bsimpan = new javax.swing.JButton();
        bbatal = new javax.swing.JButton();
        bkeluar = new javax.swing.JButton();
        jLabel44 = new javax.swing.JLabel();
        total_biaya = new javax.swing.JTextField();
        panel_nota = new javax.swing.JPanel();
        labelnot = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        id_nota = new javax.swing.JTextField();
        jtglnota = new org.jdesktop.swingx.JXDatePicker();
        jLabel1 = new javax.swing.JLabel();
        panel_teknisi = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        label_idAdmin = new javax.swing.JLabel();
        label_namaAdmin = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 153, 204));

        panel_pelanggan.setBackground(new java.awt.Color(0, 153, 204));
        panel_pelanggan.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)), "PELANGGAN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(255, 255, 255))); // NOI18N
        panel_pelanggan.setForeground(new java.awt.Color(255, 255, 255));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("ID Pelanggan");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Nama");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Alamat");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("No. Telepon");

        id_pelanggan.setBackground(java.awt.SystemColor.controlHighlight);
        id_pelanggan.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        nama_pelanggan.setBackground(java.awt.SystemColor.controlHighlight);
        nama_pelanggan.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        nama_pelanggan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nama_pelangganActionPerformed(evt);
            }
        });

        nomor_pelanggan.setBackground(java.awt.SystemColor.controlHighlight);
        nomor_pelanggan.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        almt_pelanggan.setBackground(java.awt.SystemColor.controlHighlight);
        almt_pelanggan.setColumns(20);
        almt_pelanggan.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        almt_pelanggan.setRows(5);
        jScrollPane1.setViewportView(almt_pelanggan);

        bcari_pelanggan.setText("CARI");
        bcari_pelanggan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bcari_pelangganActionPerformed(evt);
            }
        });

        jLabel53.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(255, 255, 255));
        jLabel53.setText("Member");

        member_pelanggan.setBackground(java.awt.SystemColor.controlHighlight);
        member_pelanggan.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        member_pelanggan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                member_pelangganActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_pelangganLayout = new javax.swing.GroupLayout(panel_pelanggan);
        panel_pelanggan.setLayout(panel_pelangganLayout);
        panel_pelangganLayout.setHorizontalGroup(
            panel_pelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_pelangganLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_pelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_pelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panel_pelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panel_pelangganLayout.createSequentialGroup()
                        .addComponent(id_pelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(bcari_pelanggan))
                    .addComponent(jScrollPane1)
                    .addComponent(member_pelanggan)
                    .addComponent(nama_pelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nomor_pelanggan, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_pelangganLayout.setVerticalGroup(
            panel_pelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_pelangganLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_pelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(id_pelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bcari_pelanggan))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panel_pelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(nama_pelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panel_pelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(nomor_pelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panel_pelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(member_pelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel53))
                .addGap(18, 18, 18)
                .addGroup(panel_pelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        detail_service.setBackground(new java.awt.Color(0, 153, 204));
        detail_service.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)), "LAYANAN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(255, 255, 255))); // NOI18N
        detail_service.setForeground(new java.awt.Color(255, 255, 255));

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("ID Layanan");

        id_layanan.setBackground(java.awt.SystemColor.controlHighlight);

        bcari_layanan.setText("CARI");
        bcari_layanan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bcari_layananActionPerformed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Layanan");

        jenis_layanan.setBackground(java.awt.SystemColor.controlHighlight);

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Estimasi Waktu");

        estimasi.setBackground(java.awt.SystemColor.controlHighlight);

        btambah_layanan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/signup.png"))); // NOI18N
        btambah_layanan.setText("TAMBAH");
        btambah_layanan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btambah_layananActionPerformed(evt);
            }
        });

        jLabel51.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(255, 255, 255));
        jLabel51.setText("Sub Total");

        subTotal.setBackground(java.awt.SystemColor.controlHighlight);

        harga_perkg.setBackground(java.awt.SystemColor.controlHighlight);

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("Harga per Kg");

        jPanel2.setBackground(new java.awt.Color(0, 153, 204));

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("Kode Antar");

        kode_antar.setBackground(java.awt.SystemColor.controlHighlight);

        bcari_antar.setText("CARI");
        bcari_antar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bcari_antarActionPerformed(evt);
            }
        });

        jLabel34.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setText("Tipe");

        tipe_antar.setBackground(java.awt.SystemColor.controlHighlight);
        tipe_antar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipe_antarActionPerformed(evt);
            }
        });

        jLabel36.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(255, 255, 255));
        jLabel36.setText("Biaya Tambahan");

        biaya_tambahan.setBackground(java.awt.SystemColor.controlHighlight);

        jLabel38.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(255, 255, 255));
        jLabel38.setText("Rp");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(biaya_tambahan, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(33, 33, 33)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(kode_antar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(bcari_antar))
                            .addComponent(tipe_antar)))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(kode_antar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bcari_antar))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(tipe_antar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(biaya_tambahan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel42.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(255, 255, 255));
        jLabel42.setText("Berat (Kg)");

        berat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                beratKeyReleased(evt);
            }
        });

        jLabel35.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setText("Rp");

        jLabel37.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(255, 255, 255));
        jLabel37.setText("Rp");

        javax.swing.GroupLayout detail_serviceLayout = new javax.swing.GroupLayout(detail_service);
        detail_service.setLayout(detail_serviceLayout);
        detail_serviceLayout.setHorizontalGroup(
            detail_serviceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(detail_serviceLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(detail_serviceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(detail_serviceLayout.createSequentialGroup()
                        .addGroup(detail_serviceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22)
                            .addGroup(detail_serviceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(detail_serviceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(detail_serviceLayout.createSequentialGroup()
                                .addComponent(id_layanan, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(bcari_layanan))
                            .addComponent(jenis_layanan)
                            .addComponent(estimasi)
                            .addComponent(berat, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, detail_serviceLayout.createSequentialGroup()
                                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(harga_perkg, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(detail_serviceLayout.createSequentialGroup()
                        .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(subTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(28, 28, 28)
                .addGroup(detail_serviceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btambah_layanan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        detail_serviceLayout.setVerticalGroup(
            detail_serviceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(detail_serviceLayout.createSequentialGroup()
                .addGroup(detail_serviceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(detail_serviceLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(detail_serviceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18)
                            .addComponent(id_layanan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bcari_layanan))
                        .addGap(18, 18, 18)
                        .addGroup(detail_serviceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(jenis_layanan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(detail_serviceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(estimasi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(detail_serviceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25)
                            .addComponent(harga_perkg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel35)))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(detail_serviceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42)
                    .addComponent(berat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(detail_serviceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel51)
                    .addComponent(subTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btambah_layanan)
                    .addComponent(jLabel37))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(0, 153, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)), "Transaksi", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(255, 255, 255))); // NOI18N

        tbl_transaksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Layanan", "Layanan", "Estimasi Waktu", "Harga per Kg (Rp)", "Tipe", "Biaya Tambahan (Rp)", "Berat (Kg)", "Sub Total (Rp)"
            }
        ));
        jScrollPane2.setViewportView(tbl_transaksi);

        bhapus_transaksi.setBackground(new java.awt.Color(255, 0, 0));
        bhapus_transaksi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/delete.png"))); // NOI18N
        bhapus_transaksi.setText("HAPUS");
        bhapus_transaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bhapus_transaksiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(bhapus_transaksi, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bhapus_transaksi)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        bsimpan.setBackground(new java.awt.Color(0, 204, 0));
        bsimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/save.png"))); // NOI18N
        bsimpan.setText("SIMPAN");
        bsimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bsimpanActionPerformed(evt);
            }
        });

        bbatal.setBackground(new java.awt.Color(204, 204, 204));
        bbatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/batal.png"))); // NOI18N
        bbatal.setText("BATAL");
        bbatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bbatalActionPerformed(evt);
            }
        });

        bkeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/back.png"))); // NOI18N
        bkeluar.setText("KEMBALI");
        bkeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bkeluarActionPerformed(evt);
            }
        });

        jLabel44.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(255, 255, 255));
        jLabel44.setText("Total");

        total_biaya.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(bsimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(55, 55, 55)
                        .addComponent(bbatal, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(58, 58, 58)
                        .addComponent(bkeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(total_biaya, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(panel_pelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(detail_service, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panel_pelanggan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(detail_service, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bsimpan)
                    .addComponent(bbatal)
                    .addComponent(bkeluar)
                    .addComponent(jLabel44)
                    .addComponent(total_biaya, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(34, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 118, 1060, -1));

        panel_nota.setBackground(new java.awt.Color(0, 153, 204));

        labelnot.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labelnot.setForeground(new java.awt.Color(255, 255, 255));
        labelnot.setText("ID Nota");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Tanggal");

        id_nota.setBackground(java.awt.SystemColor.controlHighlight);

        jLabel1.setBackground(new java.awt.Color(153, 204, 255));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setText("NOTA LAUNDRY");

        javax.swing.GroupLayout panel_notaLayout = new javax.swing.GroupLayout(panel_nota);
        panel_nota.setLayout(panel_notaLayout);
        panel_notaLayout.setHorizontalGroup(
            panel_notaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_notaLayout.createSequentialGroup()
                .addContainerGap(378, Short.MAX_VALUE)
                .addGroup(panel_notaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelnot, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(panel_notaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(id_nota, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtglnota, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33))
            .addGroup(panel_notaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_notaLayout.setVerticalGroup(
            panel_notaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_notaLayout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addGroup(panel_notaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(id_nota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelnot, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panel_notaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtglnota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        getContentPane().add(panel_nota, new org.netbeans.lib.awtextra.AbsoluteConstraints(376, 2, 680, 120));

        panel_teknisi.setBackground(new java.awt.Color(0, 153, 204));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("ID Admin");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Nama Admin");

        label_idAdmin.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        label_idAdmin.setForeground(new java.awt.Color(255, 255, 255));
        label_idAdmin.setText("---");

        label_namaAdmin.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        label_namaAdmin.setForeground(new java.awt.Color(255, 255, 255));
        label_namaAdmin.setText("---");

        javax.swing.GroupLayout panel_teknisiLayout = new javax.swing.GroupLayout(panel_teknisi);
        panel_teknisi.setLayout(panel_teknisiLayout);
        panel_teknisiLayout.setHorizontalGroup(
            panel_teknisiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_teknisiLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(panel_teknisiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(panel_teknisiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label_idAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label_namaAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
        );
        panel_teknisiLayout.setVerticalGroup(
            panel_teknisiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_teknisiLayout.createSequentialGroup()
                .addContainerGap(62, Short.MAX_VALUE)
                .addGroup(panel_teknisiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label_idAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panel_teknisiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label_namaAdmin, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        getContentPane().add(panel_teknisi, new org.netbeans.lib.awtextra.AbsoluteConstraints(-4, -1, 380, 120));

        jLabel45.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(255, 255, 255));
        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel45.setText(":");
        getContentPane().add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(821, 679, 19, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bhapus_transaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bhapus_transaksiActionPerformed
        int index = tbl_transaksi.getSelectedRow();
        tabmode.removeRow(index);
        tbl_transaksi.setModel(tabmode);
        hitung();
    }//GEN-LAST:event_bhapus_transaksiActionPerformed

    private void bcari_pelangganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bcari_pelangganActionPerformed
        popup.popup_pelanggan pp = new popup.popup_pelanggan();
        pp.plgn = this;
        pp.setVisible(true);
        pp.setResizable(false);
        pp.setLocationRelativeTo(null);
    }//GEN-LAST:event_bcari_pelangganActionPerformed

    private void bsimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bsimpanActionPerformed
        Date tanggalnota = jtglnota.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tn = sdf.format(tanggalnota);
        String sql = "insert into tb_nota values (?,?,?,?)";
        String zsql = "insert into tb_nota_detail values (?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setString(1, id_nota.getText());
            stat.setString(2, tn);
            stat.setString(3, label_idAdmin.getText());
            stat.setString(4, id_pelanggan.getText());

            stat.executeUpdate();

            int t = tbl_transaksi.getRowCount();
            for (int i = 0; i < t; i++) {
                String xid = tbl_transaksi.getValueAt(i, 0).toString();
                String xjns = tbl_transaksi.getValueAt(i, 1).toString();
                String xest = tbl_transaksi.getValueAt(i, 2).toString();
                String xhpk = tbl_transaksi.getValueAt(i, 3).toString();
                String xtipe = tbl_transaksi.getValueAt(i, 4).toString();
                String xbya = tbl_transaksi.getValueAt(i, 5).toString();
                String xbrt = tbl_transaksi.getValueAt(i, 6).toString();

                PreparedStatement stat2 = conn.prepareStatement(zsql);
                stat2.setString(1, id_nota.getText());
                stat2.setString(2, xid);
                stat2.setString(3, xjns);
                stat2.setString(4, xest);
                stat2.setString(5, xhpk);
                stat2.setString(6, xtipe);
                stat2.setString(7, xbya);
                stat2.setString(8, xbrt);

                stat2.executeUpdate();
            }
            JOptionPane.showMessageDialog(null, "Data berhasil disimpan");
            cetak();
            aktif();
            kosong();

            autonumber();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Data gagal disimpan" + e);
        }
        //        kosong();
        //        aktif();
        //        autonumber();
        
    }//GEN-LAST:event_bsimpanActionPerformed

    private void bbatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bbatalActionPerformed
        kosong();
        aktif();
        autonumber();
    }//GEN-LAST:event_bbatalActionPerformed

    private void bkeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bkeluarActionPerformed
        menu_utama mu = new menu_utama();
        mu.setVisible(true);
        mu.setLocationRelativeTo(null);
        this.dispose();
    }//GEN-LAST:event_bkeluarActionPerformed

    private void nama_pelangganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nama_pelangganActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nama_pelangganActionPerformed

    private void member_pelangganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_member_pelangganActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_member_pelangganActionPerformed

    private void btambah_layananActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btambah_layananActionPerformed
        try {
            String idlayanan = id_layanan.getText();
            String jenislayanan = jenis_layanan.getText();
            String estimasiwaktu = estimasi.getText();
            int hargaperkg = Integer.parseInt(harga_perkg.getText());

            String kodeantar = kode_antar.getText();
            String tipe = tipe_antar.getText();
            int biaya = Integer.parseInt(biaya_tambahan.getText());
            int brt = Integer.parseInt(berat.getText());
            int subtotallay = Integer.parseInt(subTotal.getText());

            tabmode.addRow(new Object[]{idlayanan, jenislayanan, estimasiwaktu, hargaperkg, tipe, biaya, brt, subtotallay});

            tbl_transaksi.setModel(tabmode);
        } catch (Exception e) {
            System.out.println("Error : " + e);
        }
        id_layanan.setText("");
        jenis_layanan.setText("");
        estimasi.setText("");
        harga_perkg.setText("");
        kode_antar.setText("");
        tipe_antar.setText("");
        biaya_tambahan.setText("");
        berat.setText(PLACEHOLDER_berat);
        subTotal.setText("");
        hitung();
    }//GEN-LAST:event_btambah_layananActionPerformed

    private void beratKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_beratKeyReleased
        try{
            int xhrgkg = Integer.parseInt(harga_perkg.getText());
            int xbrt = Integer.parseInt(berat.getText());
            int xbtmbh = Integer.parseInt(biaya_tambahan.getText());
            int xjml = ((xhrgkg * xbrt) + xbtmbh );
            subTotal.setText(String.valueOf(xjml));
        } catch  (NumberFormatException e) {
            subTotal.setText("0");
        }
    }//GEN-LAST:event_beratKeyReleased

    private void bcari_antarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bcari_antarActionPerformed
        popup.popup_antar pa = new popup.popup_antar();
        pa.antar = this;
        pa.setVisible(true);
        pa.setResizable(false);
        pa.setLocationRelativeTo(null);
    }//GEN-LAST:event_bcari_antarActionPerformed

    private void bcari_layananActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bcari_layananActionPerformed
        popup.popup_layanan pl = new popup.popup_layanan();
        pl.layanan = this;
        pl.setVisible(true);
        pl.setResizable(false);
        pl.setLocationRelativeTo(null);
    }//GEN-LAST:event_bcari_layananActionPerformed

    private void tipe_antarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipe_antarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tipe_antarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(cucian_masuk.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(cucian_masuk.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(cucian_masuk.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(cucian_masuk.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new cucian_masuk().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JTextArea almt_pelanggan;
    private javax.swing.JButton bbatal;
    private javax.swing.JButton bcari_antar;
    private javax.swing.JButton bcari_layanan;
    private javax.swing.JButton bcari_pelanggan;
    private javax.swing.JTextField berat;
    private javax.swing.JButton bhapus_transaksi;
    private javax.swing.JTextField biaya_tambahan;
    private javax.swing.JButton bkeluar;
    private javax.swing.JButton bsimpan;
    private javax.swing.JButton btambah_layanan;
    private javax.swing.JPanel detail_service;
    private javax.swing.JTextField estimasi;
    private javax.swing.JTextField harga_perkg;
    private javax.swing.JTextField id_layanan;
    private javax.swing.JTextField id_nota;
    public javax.swing.JTextField id_pelanggan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jenis_layanan;
    private org.jdesktop.swingx.JXDatePicker jtglnota;
    private javax.swing.JTextField kode_antar;
    private javax.swing.JLabel label_idAdmin;
    private javax.swing.JLabel label_namaAdmin;
    private javax.swing.JLabel labelnot;
    public javax.swing.JTextField member_pelanggan;
    public javax.swing.JTextField nama_pelanggan;
    public javax.swing.JTextField nomor_pelanggan;
    private javax.swing.JPanel panel_nota;
    private javax.swing.JPanel panel_pelanggan;
    private javax.swing.JPanel panel_teknisi;
    private javax.swing.JTextField subTotal;
    private javax.swing.JTable tbl_transaksi;
    private javax.swing.JTextField tipe_antar;
    private javax.swing.JTextField total_biaya;
    // End of variables declaration//GEN-END:variables
}
