package com.example.demo.mailUtil;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

    @Service
    public class EmailService {

        private final JavaMailSender mailSender;

        public EmailService(JavaMailSender mailSender) {
            this.mailSender = mailSender;
        }

        // Send simple text email
        public void sendSimpleEmail(String to, String subject, String text) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("libbyl6564@gmail.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);
        }

        // Send HTML email- can customize d
        public void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("your-email@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true indicates HTML

            mailSender.send(message);
        }

        // Send email with attachment
        public void sendEmailWithAttachment(String to, String subject, String text,
                                            String pathToAttachment) throws MessagingException {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("your-email@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
            helper.addAttachment("Invoice", file);

            mailSender.send(message);
        }
    }
