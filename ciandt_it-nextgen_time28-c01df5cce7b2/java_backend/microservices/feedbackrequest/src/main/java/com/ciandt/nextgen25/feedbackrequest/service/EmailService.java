package com.ciandt.nextgen25.feedbackrequest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;


import java.util.Properties;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username:}")
    private String sender;

    @Value("${spring.mail.password:}")
    private String password;

    public String sendEmail(String recipient, String subject, String message) {
        try {           

            JavaMailSenderImpl mailSender = (JavaMailSenderImpl) javaMailSender;
            mailSender.setUsername(sender);
            mailSender.setPassword(password);

            Properties props = mailSender.getJavaMailProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.debug", "true");

            // Montando e enviando o e-mail
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(sender);
            simpleMailMessage.setTo(recipient);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(message);
            mailSender.send(simpleMailMessage);

            return "Email sent";
        } catch (Exception e) {
            return "Error while trying to send email: " + e.getLocalizedMessage();
        }
    }
}
