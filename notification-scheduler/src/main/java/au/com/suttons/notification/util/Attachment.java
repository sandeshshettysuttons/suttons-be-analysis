package au.com.suttons.notification.util;

import java.util.Hashtable;
import javax.mail.Part;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Class to represent an email attachment. The MIME TYPES documented here have all been copied from the <a
 * href="http://www.w3schools.com/media/media_mimeref.asp">www.w3schools.com</a> website.
 */
public class Attachment {

	/** The mime type for Adobe PDF documents. */
	public static final String MIME_TYPE_PDF = "application/pdf";
	/** The mime type for MS Word documents. */
	public static final String MIME_TYPE_WORD = "application/msword";
	/** The mime type for MS RTF documents. */
	public static final String MIME_TYPE_RTF = "application/rtf";
	/** The mime type for MS Excel documents. */
	public static final String MIME_TYPE_EXCEL = "application/vnd.ms-excel";
	/** The mime type for comma separated value documents. */
	public static final String MIME_TYPE_CSV = "text/csv";
	/** The mime type for plain text documents. */
	public static final String MIME_TYPE_PLAIN = "text/plain";
	/** The mime type for HTML documents. */
	public static final String MIME_TYPE_HTML = "text/html";
	/** The mime type for GIF images. */
	public static final String MIME_TYPE_GIF = "image/gif";
	/** The mime type for JPEG images. */
	public static final String MIME_TYPE_JPEG = "image/jpeg";
	/** The mime type for ZIP file. */
	public static final String MIME_TYPE_ZIP = "application/zip";
	/** The content-id header. */
	public static final String HEADER_CONTENT_ID = "Content-ID";

	private String name;
	private String contentType;
	private byte[] content;
	private String disposition;
	private Hashtable<String, String> headers;

	/**
	 * Private constructor to ensure that all attachments have at least name and content defined.
	 */
	public Attachment() {
		this.setContentType(MIME_TYPE_HTML);
		this.setDisposition(Part.ATTACHMENT);
		this.setHeaders(new Hashtable<String, String>());
	}

	/**
	 * Constructor for a simple attachment that is a text file.
	 *
	 * @param name The name of the attachment as it appears in the email.
	 * @param content The contents of the file.
	 */
	public Attachment(String name, String content) {
		this();
		this.setName(name);
		this.setContent(content.getBytes());
	}

	/**
	 * Constructor for an attachment that is a text file, perhaps with a different mime-type such as text/html.
	 *
	 * @param name The name of the attachment as it appears in the email.
	 * @param contentType The mime type of the contents.
	 * @param content The contents of the file.
	 */
	public Attachment(String name, String contentType, String content) {
		this();
		this.setName(name);
		this.setContentType(contentType);
		this.setContent(content.getBytes());
	}

	/**
	 * Constructor for an attachment that is a binary file.
	 *
	 * @param name The name of the attachment as it appears in the email.
	 * @param contentType The mime type of the contents.
	 * @param content The contents of the file.
	 */
	public Attachment(String name, String contentType, byte[] content) {
		this();
		this.setName(name);
		this.setContentType(contentType);
		this.setContent(content);
	}

	/**
	 * Set the name of the attachment as it appears in the email.
	 * @param name The new name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the name of the attachment as it is to appear in the email.
	 * @return The name of the attchment.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Set the mime type of the attachment.
	 * @param contentType The new mime type.
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * Get the mime type of the attachment.
	 * @return The mime type.
	 */
	public String getContentType() {
		return this.contentType;
	}

	/**
	 * Set the content of this attachment.
	 * @param content The new content.
	 */
	public void setContent(byte[] content) {
		this.content = content;
	}

	/**
	 * Set the content of this attachment.
	 * @param content The new content.
	 */
	public void setContent(String content) {
		this.content = content.getBytes();
	}

	/**
	 * Get the contents of this attachment, regardless of mime type this is returned as a byte array.
	 * @return The content of this attachment.
	 */
	public byte[] getContentAsByteArray() {
		return this.content;
	}

	/**
	 * Get the contents of this attachment, regardless of mime type this is returned as a byte array.
	 * @return The content of this attachment.
	 */
	public String getContent() {
		return new String(this.content);
	}

	/**
	 * Get the disposition of this attachment.
	 * @return INLINE or ATTACHMENT, depending on the type of attachment.
	 */
	public String getDisposition() {
		return this.disposition;
	}

	/**
	 * Set the disposition of this attachment.
	 * @param disposition The new disposition.
	 */
	public void setDisposition(String disposition) {
		this.disposition = disposition;
	}

	/**
	 * Get the headers of this attachment, headers may include a Content-Id for INLINE attachments.
	 * @return The headers of this attachment.
	 */
	@SuppressWarnings("unchecked")
	public Hashtable getHeaders() {
		return this.headers;
	}

	/**
	 * Set the headers of this attachment.
	 * @param headers The new headers.
	 */
	@SuppressWarnings("unchecked")
	public void setHeaders(Hashtable headers) {
		this.headers = headers;
	}

	/**
	 * Add a new header to the existing list of headers.
	 * @param name The new header name.
	 * @param value The new header value.
	 */
	public void addHeader(String name, String value) {
		this.headers.put(name, value);
	}

	/**
	 * Remove an existing header from the existing list.
	 * @param name The name of the header to remove.
	 */
	public void removeHeader(String name) {
		if (this.headers.contains(name)) {
			this.headers.remove(name);
		}
	}

	/**
	 * Overriden toString method.
	 *
	 * @return A reflexively-built string representation of this bean.
	 */
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE).concat("[content String is: " + getContent() + "]");
	}
} // class
