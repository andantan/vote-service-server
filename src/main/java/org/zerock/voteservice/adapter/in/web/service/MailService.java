package org.zerock.voteservice.adapter.in.web.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromAddress;

    public void sendVerificationCode(String toEmail, String code) throws MailException, MessagingException {
        String subject = "[전자투표 서비스] 이메일 인증 코드입니다.";

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
        String htmlContent = getHtmlContent(code);

        helper.setTo(toEmail);
        helper.setFrom(fromAddress);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        mailSender.send(mimeMessage);
        log.info("Successed to send verification-code email to: {}", toEmail);
    }

    private String getHtmlContent(String verificationCode) {
        return "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<style>"
                + "body { font-family: Arial, sans-serif; margin: 0; padding: 20px; color: #333; }"
                + ".container { background-color: #f9f9f9; border: 1px solid #ddd; padding: 30px; border-radius: 8px; max-width: 600px; margin: auto; }"
                + ".header { font-size: 24px; font-weight: bold; color: #0056b3; margin-bottom: 20px; text-align: center; }"
                + ".content { font-size: 16px; line-height: 1.6; }"
                + ".code { font-size: 20px; font-weight: bold; color: #d63384; background-color: #fce4ec; padding: 10px 15px; border-radius: 5px; display: inline-block; margin: 20px 0; letter-spacing: 2px; }"
                + ".footer { font-size: 12px; color: #777; margin-top: 30px; text-align: center; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='container'>"
                + "<div class='header'>이메일 인증 안내</div>"
                + "<div class='content'>"
                + "<p>안녕하세요.</p>"
                + "<p>요청하신 이메일 인증을 완료하려면 아래의 인증 코드를 입력해 주세요.</p>"
                + "<div align='center'><span class='code'>" + verificationCode + "</span></div>"
                + "<p>이 코드는 5분간 유효합니다.</p>"
                + "</div>"
                + "<div class='footer'>"
                + "<p>&copy; 2025 전자투표서비스. All Rights Reserved.</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
    }
}
