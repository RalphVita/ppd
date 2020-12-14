/**
 * Lista as mensagens em um a fila JMS (sem consumi-las).
 * 
 * Assume Glassfish rodando com fila de acordo com o glassfish-resources.xml neste projeto.
 * 
 * Para rodar, incluir gf-client.jar no classpath:
 * 
 *  java -cp .:/diretoriodoglassfish/lib/gf-client.jar SimpleQueueBrowser
 */

import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueBrowser;

import com.sun.messaging.ConnectionConfiguration;

public class SimpleQueueBrowser {

	public static void main(String[] args) {

		String host = (args.length < 1) ? "127.0.0.1" : args[0];

		try {
			Logger.getLogger("").setLevel(Level.INFO);

			System.out.println("obtaining connection factory...");
			com.sun.messaging.ConnectionFactory connectionFactory = new com.sun.messaging.ConnectionFactory();
			connectionFactory.setProperty(ConnectionConfiguration.imqAddressList, host + ":7676");

			System.out.println("obtained connection factory.");

			System.out.println("obtaining queue...");
			Queue queue = new com.sun.messaging.Queue("PhysicalQueue");
			System.out.println("obtained queue.");

			JMSContext context = connectionFactory.createContext();

			QueueBrowser browser = context.createBrowser(queue);
			Enumeration msgs;

			// a cada 5s verifica se há mensagens para listar 
			while (true) {
				msgs = browser.getEnumeration();

				if (!msgs.hasMoreElements()) {
					System.out.println("No messages in queue");
				} else {
					while (msgs.hasMoreElements()) {
						Message tempMsg = (Message) msgs.nextElement();
						System.out.println("\nMessage: " + tempMsg);
					}
				}
				Thread.sleep(5000);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
