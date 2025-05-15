/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package LibraryMS;
//import com.mysql.cj.Session;
//import com.mysql.cj.protocol.Message;
import java.io.File;
import java.io.FileOutputStream;
//import java.net.Authenticator;
//import java.net.PasswordAuthentication;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;






/**
 *
 * @author USER
 */
public class BrowerFrame extends javax.swing.JFrame {

    /**
     * Creates new form StudentFrame
     */
    public BrowerFrame(String userName) {
        initComponents();
        Connect();
        BorrowerData();
        this.currentUserName = userName;
    }
 Connection con;
 PreparedStatement insert;
 
 public void Connect(){
     
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://localhost/library","root","");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BrowerFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(BrowerFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
     
     
 }
private void BorrowerData() {
    try {
        // Clear existing table data
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0);
        
        // Set up columns if they don't exist
        if (model.getColumnCount() == 0) {
            model.addColumn("Book ID");
            model.addColumn("Student Name");
            model.addColumn("Book Title");
            model.addColumn("Date Borrowed");
            model.addColumn("Date Return");
            model.addColumn("Status");
        }

        // Execute query to get all borrowing data
        String query = "SELECT bookid, studentname, book, date_borrowed, date_return,DoneBy ,Status FROM borrowing ";
        PreparedStatement stmt = con.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        // Populate table with data
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            row.add(rs.getString("bookid"));
            row.add(rs.getString("studentname"));
            row.add(rs.getString("book"));
            
            // Format dates properly
            Date borrowDate = rs.getDate("date_borrowed");
            Date returnDate = rs.getDate("date_return");
            row.add(borrowDate != null ? sdf.format(borrowDate) : "");
            row.add(returnDate != null ? sdf.format(returnDate) : "");
            row.add(rs.getString("DoneBy"));
            row.add(rs.getString("Status"));
            
            model.addRow(row);
        }

        // Add row selection listener
        table1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && table1.getSelectedRow() != -1) {
                    int row = table1.getSelectedRow();
                    
                    // Get all values from selected row
                    Id.setText(table1.getValueAt(row, 0).toString());
                    Name.setText(table1.getValueAt(row, 1).toString());
                    Book.setText(table1.getValueAt(row, 2).toString());
                    
                    try {
                        // Handle date display
                        String borrowDateStr = table1.getValueAt(row, 3).toString();
                        String returnDateStr = table1.getValueAt(row, 4).toString();
                        
                        if (!borrowDateStr.isEmpty()) {
                            dateb.setDate(sdf.parse(borrowDateStr));
                        }
                        if (!returnDateStr.isEmpty()) {
                            dater.setDate(sdf.parse(returnDateStr));
                        }
                        
                        // Handle status if column exists
                        if (table1.getColumnCount() > 5) {
                            String status = table1.getValueAt(row, 5).toString();
                            Role.setSelectedItem(status);
                        }
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error loading data: " + ex.getMessage());
    }
}

public void sendEmail(String toEmail, String subject, String body) {
    final String fromEmail = "pattypazzopatrick@gmail.com"; // your email
    final String password = "crrz itwk jdsf dziw";   // your email password or app password

    Properties props = new Properties();
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");

    Session session = Session.getInstance(props, new Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(fromEmail, password);
        }
    });

    try {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject(subject);
        message.setText(body);

        Transport.send(message);
         JOptionPane.showMessageDialog(rootPane, "Email sent successfully");
        //System.out.println("Email sent successfully");

    } catch (MessagingException e) {
        e.printStackTrace();
    }
}




private void generateReport() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Save Report");
    fileChooser.setSelectedFile(new File("Borrowing_Report.xlsx"));
    
    int userSelection = fileChooser.showSaveDialog(null);
    
    if (userSelection == JFileChooser.APPROVE_OPTION) {
        File fileToSave = fileChooser.getSelectedFile();

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Borrowing Records");

            // Table Header
            String[] headers = {"Book ID", "Student Name", "Book Title", "Date Borrowed", "Date Return", "Done By", "Status","Done At"};
            XSSFRow headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            // Database data
            String query = "SELECT bookid, studentname, book, date_borrowed, date_return, DoneBy, Status,DoneAt FROM borrowing";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            int rowIndex = 1;
            while (rs.next()) {
                XSSFRow row = sheet.createRow(rowIndex++);

                row.createCell(0).setCellValue(rs.getString("bookid"));
                row.createCell(1).setCellValue(rs.getString("studentname"));
                row.createCell(2).setCellValue(rs.getString("book"));
                row.createCell(3).setCellValue(rs.getString("date_borrowed"));
                row.createCell(4).setCellValue(rs.getString("date_return"));
                row.createCell(5).setCellValue(rs.getString("DoneBy"));
                row.createCell(6).setCellValue(rs.getString("Status"));
                 row.createCell(7).setCellValue(rs.getString("DoneAt"));
            }

            try (FileOutputStream out = new FileOutputStream(fileToSave)) {
                workbook.write(out);
                JOptionPane.showMessageDialog(null, "Report generated successfully: " + fileToSave.getAbsolutePath());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error generating report: " + ex.getMessage());
        }
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
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        Id = new javax.swing.JTextField();
        Name = new javax.swing.JTextField();
        Book = new javax.swing.JTextField();
        AddBtn = new javax.swing.JButton();
        UpdateBtn = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        dateb = new com.toedter.calendar.JDateChooser();
        dater = new com.toedter.calendar.JDateChooser();
        Role = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        totalbtn = new javax.swing.JButton();
        total = new javax.swing.JTextField();
        Search = new javax.swing.JTextField();
        SearchButton = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 0, 0));

        jLabel1.setBackground(new java.awt.Color(204, 204, 255));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Admin Borrowing Management !");

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 0, 0));
        jButton1.setText("X");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(501, 501, 501)
                .addComponent(jButton1)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jButton1))
                .addGap(17, 17, 17))
        );

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("BookId");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Student Name");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Book");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Date Borrowed");

        AddBtn.setBackground(new java.awt.Color(255, 204, 102));
        AddBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        AddBtn.setForeground(new java.awt.Color(255, 255, 255));
        AddBtn.setText("Borrow Book");
        AddBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddBtnActionPerformed(evt);
            }
        });

        UpdateBtn.setBackground(new java.awt.Color(0, 102, 255));
        UpdateBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        UpdateBtn.setForeground(new java.awt.Color(255, 255, 255));
        UpdateBtn.setText("Return Book");
        UpdateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UpdateBtnActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Date to Return");

        Role.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Borrowed", "Returned" }));
        Role.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RoleActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setText("Status:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Role, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel3)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Id, javax.swing.GroupLayout.DEFAULT_SIZE, 437, Short.MAX_VALUE)
                            .addComponent(Name)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addComponent(AddBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(UpdateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(107, 107, 107))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(63, 63, 63)
                        .addComponent(Book))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateb, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
                            .addComponent(dater, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Id, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Name, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Book, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dateb, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(dater, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Role, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                        .addGap(8, 8, 8)))
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AddBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(UpdateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29))
        );

        table1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "BookId", "StudentName", "Book", "BateBorrowed", "Date_Return", "DoneBy", "Status"
            }
        ));
        jScrollPane1.setViewportView(table1);

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/LibraryMS/student.jpeg"))); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 315, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 462, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        totalbtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        totalbtn.setText("Total Browings");
        totalbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalbtnActionPerformed(evt);
            }
        });

        SearchButton.setText("Search");
        SearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchButtonActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(51, 51, 0));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Generate Report");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(Search, javax.swing.GroupLayout.PREFERRED_SIZE, 429, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(SearchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(totalbtn)
                        .addGap(18, 18, 18)
                        .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(169, 169, 169))))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addGap(348, 348, 348))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(totalbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(total, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Search, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(SearchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void UpdateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpdateBtnActionPerformed
        // TODO add your handling code here:
try {
    String id = Id.getText();
    String name = Name.getText();
    String book = Book.getText();
    String role = Role.getSelectedItem().toString(); // Borrowed or Returned

    // 1. Check book
    PreparedStatement checkBook = con.prepareStatement(
        "SELECT Status FROM books WHERE bookid = ? AND bookname = ?");
    checkBook.setString(1, id);
    checkBook.setString(2, book);
    ResultSet bookResult = checkBook.executeQuery();

    if (!bookResult.next()) {
        JOptionPane.showMessageDialog(rootPane, "Error: Book not found in library");
        return;
    }

    // 2. Check student and get email
    PreparedStatement checkStudent = con.prepareStatement(
        "SELECT email FROM students WHERE names = ?");
    checkStudent.setString(1, name);
    ResultSet studentResult = checkStudent.executeQuery();

    String email = null;
    if (studentResult.next()) {
        email = studentResult.getString("email");
    } else {
        JOptionPane.showMessageDialog(rootPane, "Error: Student not registered");
        return;
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String date_borrowed = sdf.format(dateb.getDate());
    String date_return = sdf.format(dater.getDate());

    if ("Returned".equals(role)) {
        // 3. Check last borrowed record
        PreparedStatement checkBorrowed = con.prepareStatement(
            "SELECT borrow_id FROM borrowing WHERE bookid = ? AND Status = 'Borrowed' ORDER BY borrow_id DESC LIMIT 1");
        checkBorrowed.setString(1, id);
        ResultSet borrowedRow = checkBorrowed.executeQuery();

        if (!borrowedRow.next()) {
            JOptionPane.showMessageDialog(rootPane, "Error: Book is not currently borrowed");
            return;
        }

        int latestBorrowId = borrowedRow.getInt("borrow_id");

        // 4. Update borrowing record
        insert = con.prepareStatement(
            "UPDATE borrowing SET studentname=?, book=?, date_borrowed=?, date_return=?, Status=?, DoneBy=?, DoneAt=NOW() WHERE borrow_id=?");
        insert.setString(1, name);
        insert.setString(2, book);
        insert.setString(3, date_borrowed);
        insert.setString(4, date_return);
        insert.setString(5, role);
        insert.setString(6, currentUserName);
        insert.setInt(7, latestBorrowId);
        insert.executeUpdate();

        // Email for return
        String subject = "Book Returned Successfully";
        String msg = String.format(
            "Dear %s,\n\nYou have successfully returned the book \"%s\" (ID: %s) today (%s).\n\nThank you.\n\nUR-CST Library!",
            name, book, id, date_return);
        sendEmail(email, subject, msg);

    } else {
        // 5. Add borrowing again (re-borrow)
        insert = con.prepareStatement(
            "INSERT INTO borrowing(bookid, studentname, book, date_borrowed, date_return, Status, DoneBy, DoneAt) " +
            "VALUES(?,?,?,?,?,?,?,NOW())");
        insert.setString(1, id);
        insert.setString(2, name);
        insert.setString(3, book);
        insert.setString(4, date_borrowed);
        insert.setString(5, date_return);
        insert.setString(6, role);
        insert.setString(7, currentUserName);
        insert.executeUpdate();

        // Email for borrow
        String subject = "Book Borrowed Successfully";
        String msg = String.format(
            "Dear %s,\n\nYou have successfully borrowed the book \"%s\" (ID: %s) today (%s).\n" +
            "You are required to return it by (%s).\n\nPlease do not extend the return date, as fines will apply.\n\nUR-CST Library!",
            name, book, id, date_borrowed, date_return);
        sendEmail(email, subject, msg);
    }

    // 6. Update book status
    PreparedStatement updateBookStatus = con.prepareStatement(
        "UPDATE books SET Status = ? WHERE bookid = ?");
    updateBookStatus.setString(1, "Returned".equals(role) ? "Available" : "Borrowed");
    updateBookStatus.setString(2, id);
    updateBookStatus.executeUpdate();

    JOptionPane.showMessageDialog(rootPane, "Borrowing record processed successfully.");
    BorrowerData();

    // 7. Clear fields
    Id.setText("");
    Name.setText("");
    Book.setText("");
    dateb.setDate(null);
    dater.setDate(null);

} catch (SQLException ex) {
    Logger.getLogger(BrowerFrame.class.getName()).log(Level.SEVERE, null, ex);
    JOptionPane.showMessageDialog(rootPane, "Database Error: " + ex.getMessage());
}


    }//GEN-LAST:event_UpdateBtnActionPerformed

    private void AddBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddBtnActionPerformed
        // TODO add your handling code here:
       // TODO add your handling code here:
try {
    String id = Id.getText();
    String name = Name.getText();
    String book = Book.getText();
    String role = Role.getSelectedItem().toString(); // usually "Borrowed"
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    if (dateb.getDate() == null || dater.getDate() == null) {
        JOptionPane.showMessageDialog(rootPane, "Please select both Borrow and Return dates");
        return;
    }

    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    java.util.Date today = cal.getTime();
    java.util.Date borrowDate = dateb.getDate();
    java.util.Date returnDate = dater.getDate();

    if (borrowDate.before(today)) {
        JOptionPane.showMessageDialog(rootPane, "Error: Borrow date cannot be in the past");
        return;
    }
    if (!returnDate.after(borrowDate)) {
        JOptionPane.showMessageDialog(rootPane, "Error: Return date must be after borrow date");
        return;
    }

    String date_borrowed = sdf.format(borrowDate);
    String date_return = sdf.format(returnDate);

    // 1. Check book status
    PreparedStatement checkBook = con.prepareStatement(
        "SELECT Status FROM books WHERE bookid = ? AND bookname = ?");
    checkBook.setString(1, id);
    checkBook.setString(2, book);
    ResultSet bookResult = checkBook.executeQuery();

    if (!bookResult.next()) {
        JOptionPane.showMessageDialog(rootPane, "Error: Book not found in library");
        return;
    }

    String bookStatus = bookResult.getString("Status");
    if ("Borrowed".equals(bookStatus)) {
        JOptionPane.showMessageDialog(rootPane, "Error: Book is already borrowed");
        return;
    }

    // 2. Check if student exists and get email
    PreparedStatement checkStudent = con.prepareStatement(
        "SELECT email FROM students WHERE names = ?");
    checkStudent.setString(1, name);
    ResultSet studentResult = checkStudent.executeQuery();

    String email = null;
    if (studentResult.next()) {
        email = studentResult.getString("email");
    } else {
        JOptionPane.showMessageDialog(rootPane, "Error: Student not registered");
        return;
    }

    // 3. Insert borrowing record
    insert = con.prepareStatement(
        "INSERT INTO borrowing(bookid, studentname, book, date_borrowed, date_return, Status, DoneBy) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)");
    insert.setString(1, id);
    insert.setString(2, name);
    insert.setString(3, book);
    insert.setString(4, date_borrowed);
    insert.setString(5, date_return);
    insert.setString(6, role); // Borrowed
    insert.setString(7, currentUserName); // from MainFrame
    insert.executeUpdate();

    // 4. Update books table
    PreparedStatement updateBookStatus = con.prepareStatement(
        "UPDATE books SET Status = 'Borrowed' WHERE bookid = ?");
    updateBookStatus.setString(1, id);
    updateBookStatus.executeUpdate();

    JOptionPane.showMessageDialog(rootPane, "Borrowing Added Successfully");
    BorrowerData();

    // 5. Send Email
    String subject = "Book Borrowed Successfully";
    String msg = String.format(
        "Dear %s,\n\nYou have successfully borrowed the book \"%s\" (ID: %s) today (%s).\n" +
        "You are required to return it by (%s).\n\nPlease do not extend the return date, as fines will apply.\n\nLibrary System",
        name, book, id, date_borrowed, date_return);
    sendEmail(email, subject, msg);

    // 6. Clear fields
    Id.setText("");
    Name.setText("");
    Book.setText("");
    dateb.setDate(null);
    dater.setDate(null);

} catch (SQLException ex) {
    Logger.getLogger(BrowerFrame1.class.getName()).log(Level.SEVERE, null, ex);
    JOptionPane.showMessageDialog(rootPane, "Database Error: " + ex.getMessage());
}


    }//GEN-LAST:event_AddBtnActionPerformed

    private void totalbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalbtnActionPerformed
        // TODO add your handling code here:
         try {
            String sql="select count(book) from borrowing where Status='Borrowed'";
            insert=con.prepareStatement(sql);
            ResultSet Rs=insert.executeQuery();
            if(Rs.next()){
                String sum=Rs.getString("count(book)");
                total.setText(sum);
            }
        } catch (SQLException ex) {
            Logger.getLogger(StudentFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_totalbtnActionPerformed

    private void RoleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RoleActionPerformed

    }//GEN-LAST:event_RoleActionPerformed

    private void SearchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchButtonActionPerformed
        // TODO add your handling code here:
        String searchText = Search.getText().trim().toLowerCase();
    DefaultTableModel model = (DefaultTableModel) table1.getModel();
    
    // Clear current filtering
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
    table1.setRowSorter(sorter);
    
    if (searchText.isEmpty()) {
        sorter.setRowFilter(null);
    } else {
        // Filter across all columns
        RowFilter<DefaultTableModel, Object> filter = new RowFilter<DefaultTableModel, Object>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                for (int i = 0; i < entry.getValueCount(); i++) {
                    if (entry.getStringValue(i).toLowerCase().contains(searchText)) {
                        return true;
                    }
                }
                return false;
            }
        };
        sorter.setRowFilter(filter);
    }
    }//GEN-LAST:event_SearchButtonActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        //download report
        
        generateReport();
    
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(BrowerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(BrowerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(BrowerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(BrowerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//               // new BrowerFrame().setVisible(true);
//            }
//        });
//    }
 private String currentUserName;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddBtn;
    private javax.swing.JTextField Book;
    private javax.swing.JTextField Id;
    private javax.swing.JTextField Name;
    private javax.swing.JComboBox<String> Role;
    private javax.swing.JTextField Search;
    private javax.swing.JButton SearchButton;
    private javax.swing.JButton UpdateBtn;
    private com.toedter.calendar.JDateChooser dateb;
    private com.toedter.calendar.JDateChooser dater;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable table1;
    private javax.swing.JTextField total;
    private javax.swing.JButton totalbtn;
    // End of variables declaration//GEN-END:variables
}
