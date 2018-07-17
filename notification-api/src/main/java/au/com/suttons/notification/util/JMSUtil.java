package au.com.suttons.notification.util;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;

@RequestScoped
public class JMSUtil {

	public static final String MONTHEND_INVOICE_CREATION_QUEUE = "java:/jms/queue/MonthendInvoiceCreationQueue";

	@Resource(mappedName = MONTHEND_INVOICE_CREATION_QUEUE)
	private Queue monthendInvoiceCreationQueue;

    @Resource(mappedName = "java:/JmsXA")
    private ConnectionFactory cf;

    private Connection connection;

    public void sendMessage(String destination, Serializable object) throws Throwable {

		try {         
			connection = cf.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageProducer publisher = session.createProducer(this.getQueue(destination));

			connection.start();

			ObjectMessage message = session.createObjectMessage(object);
			publisher.send(message);

		} catch (Throwable e) {
			e.printStackTrace();
			throw e;
		} finally {         
			if (connection != null)   {
				try {
					connection.close();
				} catch (JMSException e) {                    
					e.printStackTrace();
				}
			}
		}
    }

	public Queue getQueue(String destination) throws Exception {
		switch(destination) {
			case MONTHEND_INVOICE_CREATION_QUEUE:
				return monthendInvoiceCreationQueue;
			default:
				throw new IllegalArgumentException("Unknown destination");
		}
	}
}
