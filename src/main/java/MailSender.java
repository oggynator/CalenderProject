import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.MailcapCommandMap;
import javax.activation.MimetypesFileTypeMap;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Properties;

/**
 * Created by mahesh on 1/20/17.
 */
public class MailSender {

    public void sendMail(String inviteMessage, String method, List<String> emails) {
        try {

            Properties prop = new Properties();
            prop.put("mail.smtp.starttls.enable", "true");
            prop.put("mail.smtp.auth", "true");
            Session session = Session.getDefaultInstance(prop, null);

            //headers for calendar invite
            MimeMessage message = new MimeMessage(session);
            message.addHeaderLine("charset=UTF-8");
            message.addHeaderLine("component=VEVENT");
            message.addHeaderLine("method=" + method);

            message.setFrom(new InternetAddress("abc@xyz.com"));
            message.setSubject("Comptetencehouse aftale");

            for(String email: emails) {
                message.addRecipients(Message.RecipientType.TO, email);
            }

            BodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setContent("Dette er en kalender begivenhed for din aftale.", "text/plain;charset=\"UTF-8\"");

            //invite's calendar part
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(inviteMessage, "text/calendar;charset=\"UTF-8\";method=" + method);

            Multipart multipart = new MimeMultipart("alternative");
            multipart.addBodyPart(textBodyPart);
            multipart.addBodyPart(messageBodyPart);

            Multipart mixedMultipart = new MimeMultipart("mixed");
            BodyPart mixedBodyPart = new MimeBodyPart();
            mixedBodyPart.setContent(multipart);
            mixedMultipart.addBodyPart(mixedBodyPart);

            //attachment metadata for mail
            BodyPart emptyBodyPart = new MimeBodyPart();
            emptyBodyPart.addHeader("Content-Type", "application/ics; name=\"invite.ics\"");
            emptyBodyPart.addHeader("Content-Disposition", "attachment; filename=\"invite.ics\"");
            emptyBodyPart.addHeader("Content-Transfer-Encoding", "base64");
            emptyBodyPart.setContent(inviteMessage, "application/ics; name=\"invite.ics\"");

            mixedMultipart.addBodyPart(emptyBodyPart);

            message.setContent(mixedMultipart);

            Transport transport = session.getTransport ("smtp");
            transport.connect("smtp.gmail.com","easycutmail@gmail.com","FeldtogHejberg2020!"); //outgoing mail info
            transport.sendMessage(message, message.getAllRecipients ());
            transport.close ();
        } catch (MessagingException me) {
            me.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}