package com.zenika.zencontact.email;

import com.zenika.zencontact.resource.auth.AuthentificationService;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by maeln on 01/03/17.
 */
public class EmailService {
	private static final Logger LOG = Logger.getLogger("EMAIL");
	private static EmailService INSTANCE = new EmailService();
	public static EmailService instance() {
		return INSTANCE;
	}

	public void sendEmail(Email mail) {
		LOG.warning(String.valueOf(mail));
		Properties prop = new Properties();
		Session session = Session.getDefaultInstance(prop);
		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(AuthentificationService.getInstance().getUser().getEmail(),
					AuthentificationService.getInstance().getUser().getNickname()));
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(mail.to, mail.toName));
			msg.setReplyTo(new Address[] {
					new InternetAddress("team@zencontact-160109.appspotmail.com", "Team")
			});

			msg.setSubject(mail.subject);
			msg.setText(mail.body);
			Transport.send(msg);
			LOG.warning("Email sent.");
		} catch (Exception e) {
			LOG.warning(e.toString());
		}
	}

	public void logEmail(HttpServletRequest request) {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		try {
			//MimeMessage encapsule toutes les informations
			MimeMessage message = new MimeMessage(session,
					request.getInputStream());

			LOG.warning("Subject:" + message.getSubject()); //Sujet du mail

			Multipart multipart = (Multipart) message.getContent();
			BodyPart part = multipart.getBodyPart(0);
			LOG.warning("Body:" + (String) part.getContent()); //Contenu du mail

			for (Address sender : message.getFrom()) {
				LOG.warning("From:" + sender.toString()); //Origine du mail
			}
		} catch (MessagingException e) {
		} catch (IOException e) {}
	}
}
