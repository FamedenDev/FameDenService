package com.fameden.util.mail;

import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.fameden.common.constants.CommonConstants;
import com.sun.net.ssl.internal.ssl.Provider;

/**
 * 
 * @author Rav
 */
@SuppressWarnings("restriction")
public class SendMail implements Runnable {

	private String SMTP_HOST_NAME;
	private String SMTP_PORT;
	private String debug;
	private String auth;
	private String MsgTxt = null;
	private String Subject = null;
	private String From = null;
	private String pwd = null;
	private String[] too;
	private String[] attachements;
	private Thread th;

	public static void main(String[] args) {
		String[] to = { "ravjot28@gmail.com" };
		String[] at = { "/Users/ravjotsingh/Documents/workspace/Test/hiveresultset.csv" };
		SendMail sm = new SendMail(CommonConstants.FAMEDEN_INFO_GMAIL_USER_NAME, CommonConstants.FAMEDEN_INFO_GMAIL_PASSWORD,
				"TestWithAttachement", "Test Message", at, to);
		sm.send();

	}

	public SendMail(String hostName, String portNmber, String debug,
			String auth, String from, String password, String sub, String msg,
			String[] attachments, String[] to) {
		this.SMTP_HOST_NAME = hostName;
		this.SMTP_PORT = portNmber;
		this.debug = debug;
		this.auth = auth;
		this.MsgTxt = msg;
		this.Subject = sub;
		this.From = from;
		this.pwd = password;
		this.too = to;
		this.attachements = attachments;
	}

	public SendMail(String from, String password, String sub, String msg,
			String[] attachments, String[] to) {
		this.SMTP_HOST_NAME = CommonConstants.GMAIL_SMTP_HOST_NAME;
		this.SMTP_PORT = CommonConstants.GMAIL_SMTP_PORT;
		this.debug = Boolean.toString(true);
		this.auth = Boolean.toString(true);
		this.MsgTxt = msg;
		this.Subject = sub;
		this.From = from;
		this.pwd = password;
		this.too = to;
		this.attachements = attachments;
	}

	public SendMail(String sub, String msg, String[] to) {
		this(sub, msg, null, to);
	}

	public SendMail(String sub, String msg, String[] attachments, String[] to) {
		this.SMTP_HOST_NAME = CommonConstants.GMAIL_SMTP_HOST_NAME;
		this.SMTP_PORT = CommonConstants.GMAIL_SMTP_PORT;
		this.debug = Boolean.toString(true);
		this.auth = Boolean.toString(true);
		this.MsgTxt = msg;
		this.Subject = sub;
		this.From = CommonConstants.FAMEDEN_INFO_GMAIL_USER_NAME;
		this.pwd = CommonConstants.FAMEDEN_INFO_GMAIL_PASSWORD;
		this.too = to;
		this.attachements = attachments;
	}

	public void send() {
		th = new Thread(this);
		th.start();
	}

	private void sendSSLMessage(String[] recipients, String subject,
			String message, String from, String pwd, String[] attachement)
			throws MessagingException, UnsupportedEncodingException {
		boolean debug = false;
		Properties props = new Properties();
		props.put("mail.smtp.host", SMTP_HOST_NAME);
		props.put("mail.smtp.auth", auth);
		props.put("mail.debug", this.debug);
		props.put("mail.smtp.port", SMTP_PORT);
		props.put("mail.smtp.socketFactory.port", SMTP_PORT);
		props.put("mail.smtp.socketFactory.class", CommonConstants.SSL_FACTORY);
		props.put("mail.smtp.socketFactory.fallback", Boolean.toString(false));
		final String from1 = from;
		final String pwd1 = pwd;
		Session session = Session.getDefaultInstance(props,
				new Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(from1, pwd1);
					}
				});
		session.setDebug(debug);

		MimeMessage msg = new MimeMessage(session);
		InternetAddress addressFrom = new InternetAddress(from,
				CommonConstants.FAMEDEN_ORG_NAME);
		msg.setFrom(addressFrom);
		InternetAddress[] addressTo = new InternetAddress[recipients.length];
		for (int i = 0; i < recipients.length; i++) {
			addressTo[i] = new InternetAddress(recipients[i]);
		}
		msg.setRecipients(Message.RecipientType.TO, addressTo);

		msg.setSubject(subject);

		MimeBodyPart mbp1 = new MimeBodyPart();
		mbp1.setText(message);
		Multipart mp = new MimeMultipart();
		mp.addBodyPart(mbp1);
		if (attachement != null && attachement.length > 0) {
			MimeBodyPart[] attachment = new MimeBodyPart[attachement.length];
			for (int i = 0; i < attachement.length; i++) {
				attachment[i] = new MimeBodyPart();
				FileDataSource fds = new FileDataSource(attachement[i]);
				attachment[i].setDataHandler(new DataHandler(fds));
				attachment[i].setFileName(fds.getName());
			}
			for (int j = 0; j < attachement.length; j++) {
				mp.addBodyPart(attachment[j]);
			}

		}

		msg.setContent(mp);

		Transport.send(msg);
	}

	@Override
	public void run() {
		Security.addProvider(new Provider());
		try {
			sendSSLMessage(this.too, this.Subject, this.MsgTxt, this.From,
					this.pwd, this.attachements);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
