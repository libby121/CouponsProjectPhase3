package com.example.demo.mailUtil;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationService {

@Autowired
private EmailService emailService;

public void sendWelcomeEmail(String userEmail, String userName) {
String subject = "Welcome to Our Platform!";
String htmlContent = "<h1>Welcome " + userName + "!</h1>" +
"<p>Thank you for joining our platform.</p>" +
"<p>Best regards,<br>The Team</p>";

try {
emailService.sendHtmlEmail(userEmail, subject, htmlContent);
} catch (MessagingException e) {
// Handle exception
e.printStackTrace();
}
}
}
