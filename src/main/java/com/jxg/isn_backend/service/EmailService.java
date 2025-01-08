package com.jxg.isn_backend.service;

import jakarta.mail.MessagingException;

public interface EmailService {

    void sendHtmlEmail(String to, String subject, String bodyHtml) throws MessagingException;
}
