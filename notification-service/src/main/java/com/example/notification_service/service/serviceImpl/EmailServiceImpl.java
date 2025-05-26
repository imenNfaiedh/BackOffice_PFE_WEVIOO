package com.example.notification_service.service.serviceImpl;


import java.io.File;
import java.util.Map;


import com.example.notification_service.entities.EmailDetails;
import com.example.notification_service.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

import org.thymeleaf.context.Context;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    //SpringTemplateEngine est le moteur Thymeleaf pour générer le contenu HTML
    private SpringTemplateEngine templateEngine;
    @Value("${spring.mail.username}") private String sender;

    public String sendHtmlMailWithTemplate(EmailDetails details, Map<String, Object> model) {
        try {
            //Context contient les variables Thymeleaf
            Context context = new Context();
            context.setVariables(model);
            String htmlContent = templateEngine.process("fraud-alert", context);

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            //Remplir les infos de l’email
            helper.setFrom(sender);
            helper.setTo(details.getRecipient());
            helper.setSubject(details.getSubject());
            helper.setText(htmlContent, true); // true = HTML

            javaMailSender.send(mimeMessage);
            return " Mail Sent Successfully";
        } catch (Exception e) {
            return "Error while sending HTML email";

        }
    }

    public String sendSimpleMail(EmailDetails details)
    {
        try {

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());

            javaMailSender.send(mailMessage);
            return "Mail Sent Successfully...";
        }

        catch (Exception e) {
            return "Error while Sending Mail";
        }
    }


    public String
    sendMailWithAttachment(EmailDetails details)
    {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {

            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(details.getRecipient());
            mimeMessageHelper.setText(details.getMsgBody());
            mimeMessageHelper.setSubject(details.getSubject());


            FileSystemResource file = new FileSystemResource(new File(details.getAttachment()));

            mimeMessageHelper.addAttachment(file.getFilename(), file);

            // Sending the mail
            javaMailSender.send(mimeMessage);
            return "Mail sent Successfully";
        }


        catch (MessagingException e) {

            return "Error while sending mail!!!";
        }
    }
}
