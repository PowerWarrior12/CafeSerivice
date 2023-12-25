package com.simbirsoft.mail;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class MessageSender {
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine thymeLeafTemplate;

    @Value("${spring.mail.username}")
    private String username;

    @SneakyThrows
    public void sendMessage(String toEmail, String subject, String template, Map<String, Object> attributes) {
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(attributes);
        String htmlBody = thymeLeafTemplate.process(template, thymeleafContext);
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, MESSAGE_ENCODING);
        mimeMessageHelper.setFrom(username);
        mimeMessageHelper.setTo(toEmail);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(htmlBody, true);
        javaMailSender.send(message);
    }

    private final static String MESSAGE_ENCODING = "UTF-8";
}
