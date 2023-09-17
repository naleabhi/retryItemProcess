package com.common.retryProcess.service;

import com.common.retryProcess.configuration.EmailDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.Map;

@Service
public class NotificationServiceImpl implements NotificationService {

    Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromAddress;

    @Override
    public void sendEmail(String item, EmailDetails emailDetails) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(fromAddress);
            simpleMailMessage.setTo(emailDetails.getToAddress());
            simpleMailMessage.setSubject(emailDetails.getSubject());
            simpleMailMessage.setText(emailDetails.getContent().replace("{item}", item));
            javaMailSender.send(simpleMailMessage);
            logger.info("Message successfully sent to the: {}", emailDetails.getToAddress());
        } catch (Exception ex) {
            logger.error("Error occurred while sending msg: {}", ex.toString());
        }

    }

    @Override
    public void sendEmailReceipt(Map<String, Integer> item, EmailDetails emailDetails, int amount) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            message.setFrom(fromAddress);
            message.setRecipients(MimeMessage.RecipientType.TO,emailDetails.getToAddress().toString());
            message.setSubject(emailDetails.getSubject());
            String content = generateContent(item, amount);
            message.setText(content,"text/html; charset=utf-8");
            javaMailSender.send(message);
            logger.info("Message successfully sent to the: {}", emailDetails.getToAddress());
        } catch (Exception ex) {
            logger.error("Error occurred while sending msg: {}", ex.toString());
        }

    }

    private String generateContent(Map<String, Integer> item, int totalAmount) {
        String text =
                "<html><body><table width='100%' border='1' align='center'>"
                        + "<tr align='center'>"
                        + "<td><b>Item Name <b></td>"
                        + "<td><b>Quantity<b></td>"
                        + "</tr>";

        for (Map.Entry entry : item.entrySet())
        {
            text = text + "<tr align='center'>" + "<td>" + entry.getValue() + "</td>"
                    + "<td>" + entry.getKey() + "</td>" + "</tr></table>";
        }
        text+="<h1>Total Bill is: </h1>"+totalAmount;
        return text+"</body></html>";
    }
}
