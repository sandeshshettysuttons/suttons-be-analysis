package au.com.suttons.notification.util;

import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import au.com.suttons.notification.config.AppConfig;



public class MailerUtil
{
	
    /** The header for receipt notification */
	private static final String HEADER_RETURN_RECEIPT_TO = "Return-Receipt-To";
	/** The header for read receipt notification */
	private static final String HEADER_DISPOSITION_NOTIFICATION_TO = "Disposition-Notification-To";

    /**
	 * Send a simple email to multiple users, attachments can be included and a read receipt can be requested.
	 *
	 * @param from The email address of the sender of the message.
	 * @param toRecipients The list of recipients to place in the TO field.
	 * @param ccRecipients The list of recipients to place in the CC field.
	 * @param bccRecipients The list of recipients to place in the BCC field.
	 * @param subject The subject of this message.
	 * @param bodyText The body text of this message.
	 * @param attachments The list of attachments to include with this message.
	 * @param readReceiptRequired True if a read recipt notification is required, false otherwise.
	 * @throws javax.mail.MessagingException If there is a problem with the mail server while sending the message.
	 */
	public static void sendMail(String from, String[] toRecipients, String[] ccRecipients, String[] bccRecipients, String subject, String bodyText,
			String bodyMimeType, Attachment[] attachments, boolean readReceiptRequired) throws MessagingException
    {
		int numberOfRecipients = toRecipients.length + ccRecipients.length + bccRecipients.length;
		if (numberOfRecipients == 0) {
			throw new IllegalArgumentException("You must specify at least one recipient.");
		}

		// Get the options, and also get the current date/time. We do it once here so just in case we cross a second boundary
		// while processing, we know the filename and the sent time will live. Get the fromAddress from Options.

		// Construct Properties JavaMail needs.
		Properties props = System.getProperties();
		props.put("mail.transport.protocol", AppConfig.mailTransportProtocal);   //<transport-protocol>smtp</transport-protocol>
		props.put("mail.smtp.host", AppConfig.mailHostAddress);   //<server>mail.iinet.net.au</server>
		props.put("mail.smtp.port",AppConfig.mailSmtpPort); //<port>25</port>
		props.put("mail.smtp.starttls.enable", AppConfig.mailSmtpStarttlsEnable);    //<mail-smtp-starttls-enable>true</mail-smtp-starttls-enable>

		// Create a JavaMail message.
		Session session = Session.getDefaultInstance(props, null);
		Message msg = new MimeMessage(session);

		// Setup who the mail is from.
		InternetAddress addressFrom = new InternetAddress(from);
		msg.setFrom(addressFrom);

		// Setup any To recipients.
		InternetAddress[] addressTo = new InternetAddress[toRecipients.length];
		for (int i = 0; i < toRecipients.length; i++) {
			addressTo[i] = new InternetAddress(toRecipients[i]);
		}
		msg.setRecipients(javax.mail.Message.RecipientType.TO, addressTo);

		// Setup any CC recipients.
		InternetAddress[] addressCC = new InternetAddress[ccRecipients.length];
		for (int i = 0; i < ccRecipients.length; i++) {
			addressCC[i] = new InternetAddress(ccRecipients[i]);
		}
		msg.setRecipients(javax.mail.Message.RecipientType.CC, addressCC);

		// Setup any BCC recipients.
		InternetAddress[] addressBCC = new InternetAddress[bccRecipients.length];
		for (int i = 0; i < bccRecipients.length; i++) {
			addressBCC[i] = new InternetAddress(bccRecipients[i]);
		}
		msg.setRecipients(javax.mail.Message.RecipientType.BCC, addressBCC);

		// Setup the sent date.
		msg.setSentDate(new Date());

		// Setup the subject.
		msg.setSubject(subject);
		// Set the header for read receipt functionality.
		// This is only done when requested by the sender, and if the 'from' address isn't the do-not-reply mailbox.
		// The receiving email client has to support this functionality in order for this to work.
		if ((readReceiptRequired) && (!from.equalsIgnoreCase(AppConfig.mailSystemFromAddress)))
        {
			// This should flag when the mail is delivered to the inbox.
			msg.setHeader(HEADER_RETURN_RECEIPT_TO, from);
			// This should flag when the mail is actually read.
			msg.setHeader(HEADER_DISPOSITION_NOTIFICATION_TO, from);
		}
		// Set the email body as plain document or an html document
		if (bodyMimeType == null)
        {
			bodyMimeType = isHTMLBody(bodyText) ? Attachment.MIME_TYPE_HTML : Attachment.MIME_TYPE_PLAIN;
		}

		// Create the email body (with or without attachments)
		if (attachments != null && attachments.length > 0)
        {
			createBodyWithAttachments(msg, bodyText, bodyMimeType, attachments);
		}
		else
        {
			createBody(msg, bodyText, bodyMimeType);
		}

		// Send the message.
		Transport.send(msg);
	}

    /**
	 * Determine if the passed in message body contains common HTML tags.
	 *
	 * @param bodyText The body of a message.
	 * @return True if the bodyText contains common HTML tags, False otherwise.
	 */
	private static boolean isHTMLBody(String bodyText) {
		String message = bodyText.toLowerCase();
		// Check for common HTML tags, most important/likely tags first.
		return (message.indexOf("<html>") != -1) || (message.indexOf("<body>") != -1) || (message.indexOf("<table>") != -1) ||
				(message.indexOf("<font>") != -1) || (message.indexOf("<p>") != -1);
	}

    /**
	 * Create the body of a message that doesn't contain any attachments.
	 *
	 * @param msg The email message being created.
	 * @param bodyText The body text of this message.
	 * @param bodyMimeType The mime type of the body of this message.
	 * @throws javax.mail.MessagingException If there is a problem with the mail server while sending the message.
	 */
	private static void createBody(Message msg, String bodyText, String bodyMimeType) throws MessagingException {
		// Setup just the body.
		msg.setContent(bodyText, bodyMimeType);
	}

    /**
	 * Create the body of a message that does contain attachments.
	 *
	 * @param msg The email message being created.
	 * @param bodyText The body text of this message.
	 * @param bodyMimeType The mime type of the body of this message.
	 * @param attachments The list of attachments to include with this message.
	 * @throws javax.mail.MessagingException If there is a problem with the mail server while sending the message.
	 */
	@SuppressWarnings("unchecked")
	private static void createBodyWithAttachments(Message msg, String bodyText, String bodyMimeType, Attachment[] attachments) throws MessagingException
    {
		MimeMultipart mp = new MimeMultipart("related");

		// Add the body to the message.
		MimeBodyPart body = new MimeBodyPart();
		body.setContent(bodyText, bodyMimeType);
		mp.addBodyPart(body);

		// Add each attachment to the message.
		for (int i = 0; i < attachments.length; i++)
        {
			Attachment attachment = attachments[i];
			if (attachment != null)
            {
				MimeBodyPart attach = new MimeBodyPart();
				attach.setFileName(attachment.getName());

				if (attachment.getDisposition().equalsIgnoreCase(Part.INLINE))
                {
					// If we have inline attachments the body must be HTML.
					if (!bodyMimeType.equalsIgnoreCase(Attachment.MIME_TYPE_HTML))
                    {
						//log.error("The content type of the body must be " + Attachment.MIME_TYPE_HTML + " in order to include inline attachments.");
						throw new MessagingException("The content type of the body must be " + Attachment.MIME_TYPE_HTML + " in order to include inline attachments.");
					}
					// This is an inline attachment so setup headers.
					attach.setDisposition(attachment.getDisposition());
					Hashtable headers = attachment.getHeaders();
					if (headers.size() > 0)
                    {
						Enumeration keys = headers.keys();
						while (keys.hasMoreElements())
                        {
							String key = (String) keys.nextElement();
							attach.addHeader(key, (String) headers.get(key));
						}
					}
				}

				attach.setDataHandler(new DataHandler(new ByteArrayDataSource(attachment.getContentAsByteArray(), attachment.getContentType())));
				mp.addBodyPart(attach);
			}
			else
            {
				//log.warn("Attachment number " + (i + 1) + " for email (" + msg.getSubject() + ") was null.");
			}
		}

		msg.setContent(mp);
	}
}
