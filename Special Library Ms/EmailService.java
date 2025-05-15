/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LibraryMS;

/**
 *
 * @author USER
 */
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.JOptionPane;

public class EmailService {
    private final String host = "smtp.gmail.com";
    private final String port = "587";
    private final String username = "yourlibrary@gmail.com"; // Change this
    private final String password = "yourpassword"; // Change this
    
    public void sendBorrowConfirmation(String toEmail, String studentName, 
                                     String bookName, String bookId, 
                                     String borrowDate, String returnDate) {
        String subject = "Book Borrowing Confirmation";
        String body = String.format(
            "Dear %s,\n\n" +
            "You have successfully borrowed the book called '%s' with ID %s on %s.\n" +
            "You are required to return it by %s.\n\n" +
            "Please do not extend returning time because there will be fines for late returns.\n\n" +
            "Thank you,\nLibrary Management System",
            studentName, bookName, bookId, borrowDate, returnDate);
        
        sendEmail(toEmail, subject, body);
    }
    
    public void sendReturnConfirmation(String toEmail, String studentName, 
                                     String bookName, String bookId) {
        String subject = "Book Return Confirmation";
        String body = String.format(
            "Dear %s,\n\n" +
            "You have successfully returned the book called '%s' with ID %s.\n\n" +
            "Thank you for using our library services.\n\n" +
            "Best regards,\nLibrary Management System",
            studentName, bookName, bookId);
        
        sendEmail(toEmail, subject, body);
    }
    
    private void sendEmail(String toEmail, String subject, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username, "Library System"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);
            
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            // You might want to show this in your JFrame
            JOptionPane.showMessageDialog(null, 
                "Failed to send email: " + e.getMessage(), 
                "Email Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}