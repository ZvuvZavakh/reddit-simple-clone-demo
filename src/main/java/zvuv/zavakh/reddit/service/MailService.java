package zvuv.zavakh.reddit.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import zvuv.zavakh.reddit.exception.RedditException;
import zvuv.zavakh.reddit.model.NotificationEmail;

@Slf4j
@Service
public class MailService {

    private final JavaMailSender mailSender;
    private final MailContentBuilder mailContentBuilder;

    @Autowired
    public MailService(JavaMailSender mailSender, MailContentBuilder mailContentBuilder) {
        this.mailSender = mailSender;
        this.mailContentBuilder = mailContentBuilder;
    }

    @Async
    public void sendMail(NotificationEmail notificationEmail) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            String text = mailContentBuilder.build(notificationEmail.getBody());

            messageHelper.setFrom("test@test.test");
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(text);
        };

        try {
            mailSender.send(messagePreparator);
            log.info("Mail sent to {}", notificationEmail);
        } catch (MailException e) {
            throw new RedditException("Error occured while sending mail");
        }
    }
}
