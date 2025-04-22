package com.example.notification_service.service;

import com.example.notification_service.entities.EmailDetails;

import java.util.Map;

public interface EmailService {

    String sendSimpleMail(EmailDetails details);
    String sendMailWithAttachment(EmailDetails details);
    public String sendHtmlMailWithTemplate(EmailDetails details, Map<String, Object> model);
}
