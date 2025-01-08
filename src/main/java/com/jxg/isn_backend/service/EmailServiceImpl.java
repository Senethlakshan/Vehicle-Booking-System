package com.jxg.isn_backend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService{

    @Value("${spring.main.sender.email}")
    private String SENDER_EMAIL;

    private final JavaMailSender mailSender;


    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendHtmlEmail(String to, String subject, String bodyHtml) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();


        message.setFrom(new InternetAddress(this.SENDER_EMAIL));
        message.setRecipients(MimeMessage.RecipientType.TO, to);
        message.setSubject(subject);
        message.setContent(bodyHtml, "text/html; charset=utf-8");

        mailSender.send(message);
    }
}
