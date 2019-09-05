package kr.cibusiter.foodplanner.conponent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Properties;

@Component
@Slf4j
public class SendMailComp {

    @Value("${smtp.auth.id}")
    private String id;
    @Value("${smtp.auth.passwd}")
    private String passWd;

    /**
     * 메일 발송
     * @param subject
     * @param content
     * @param to
     * @param fromMap
     * @return
     */
    public boolean sendMail(String subject, String content, String to, Map<String, Object> fromMap){
        boolean result = false;
        boolean debug = false;

        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "");
        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");


        Authenticator auth = new SMTPAuthenticator();

        //session 생성 및  MimeMessage생성
        Session session = Session.getDefaultInstance(props, auth);
        session.setDebug(debug); //?

        MimeMessage msg = new MimeMessage(session);
        StringBuilder sbContent = new StringBuilder();

        try{
            InternetAddress addressFrom = new InternetAddress(String.valueOf(fromMap.get("email")), String.valueOf(fromMap.get("name")), "UTF-8");
            // 이메일 발신자
            msg.setFrom(addressFrom);
            // 이메일 수신자
            InternetAddress addressTo = new InternetAddress(to);
            msg.setRecipient(Message.RecipientType.TO, addressTo);
            msg.setSubject(new String(subject.getBytes("UTF-8")));
            msg.setHeader("Content-Transfer-Encoding", "quoted-printable");

            MimeMultipart multipart = new MimeMultipart("related");
            BodyPart messageBodyPartContent = new MimeBodyPart();
            sbContent.append("<div>");
            sbContent.append(content);
            sbContent.append("</div>");
            messageBodyPartContent.setContent(sbContent.toString(), "text/html; charset=UTF-8");
            multipart.addBodyPart(messageBodyPartContent);

            BodyPart messageBodyPart = new MimeBodyPart();

            DataSource fds = new FileDataSource((File)fromMap.get("file"));
            messageBodyPart.setDataHandler(new DataHandler(fds));
            messageBodyPart.setFileName(MimeUtility.encodeText(String.valueOf(fromMap.get("pdfFileName"))));
            messageBodyPart.setHeader("Content-Type", "application/octet-stream;charset=utf-8");
            messageBodyPart.setHeader("Content-Disposition", "attachment;filename="+MimeUtility.encodeText(String.valueOf(fromMap.get("pdfFileName")))+";");
            multipart.addBodyPart(messageBodyPart);

            msg.setContent(multipart);
            Transport.send(msg);
            result = true;
        }catch (MessagingException | UnsupportedEncodingException e) {
            log.error("메일발송 중 에러발생", e);
            result = false;
        }

        return result;
    }

    /**
     * 사용자 인증
     */
    private class SMTPAuthenticator extends javax.mail.Authenticator {
        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            String username = id;
            String password = passWd;
            return new PasswordAuthentication(username, password);
        }
    }
}


