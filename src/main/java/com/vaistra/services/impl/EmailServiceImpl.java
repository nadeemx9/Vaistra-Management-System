package com.vaistra.services.impl;

import com.vaistra.services.EmailService;
import com.vaistra.utils.AppUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    @Value("${host_url}")
    private String host;

    @Value("${spring.mail.username}")
    private String fromEmail;


    @Override
    @Async
    public void sendSimpleMailMessage(String name, String to, String token) {
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject("NEW USER ACCOUNT VERIFICATION");
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setText(AppUtils.getEmailMessage(name, host, token));
            emailSender.send(message);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}
