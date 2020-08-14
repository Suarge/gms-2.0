package gms.cuit.service;

import gms.cuit.dto.MailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

/**
 * @author story
 * @date 2020/5/22
 */
@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;

    static {
        System.setProperty("mail.mime.splitlongparameters", "false");
    }

    /**
     * 发送简单文本文件
     */
    @Async
    public void sendSimpleEmail(final MailDto dto){
        try {
            SimpleMailMessage message=new SimpleMailMessage();
            message.setFrom(env.getProperty("mail.send.from"));
            message.setTo(dto.getTos());
            message.setSubject(dto.getSubject());
            message.setText(dto.getContent());
            mailSender.send(message);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 发送带html邮件
     * @param dto
     */
    @Async
    public void sendHTMLMail(final MailDto dto){
        try {
            MimeMessage message=mailSender.createMimeMessage();
            MimeMessageHelper messageHelper=new MimeMessageHelper(message,true,"utf-8");
            messageHelper.setFrom(env.getProperty("mail.send.from"));
            messageHelper.setTo(dto.getTos());
            messageHelper.setSubject(dto.getSubject());
            messageHelper.setText(dto.getContent(),true);

            mailSender.send(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
