/**
 * Exemplo de uso de JMS para um chat simples, adaptando exemplo realizado anteriormente com Java RMI.
 * 
 * Instalar Glassfish, rodar:
 * asadmin start-domain 
 * E criar os recursos JMS rodando:
 * asadmin add-resources glassfish-resources.xml
 * (o arquivo XML encontra-se nesse projeto)
 * 
 * 
 * Para rodar, incluir gf-client.jar no classpath:
 * 
 *  java -cp .:/diretoriodoglassfish/lib/gf-client.jar br.ufes.inf.nemo.chatclient.Chat
 */

package br.ufes.inf.nemo.chatclient;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;



import javax.jms.*;
import com.sun.messaging.ConnectionConfiguration;

public class Chat implements MessageListener {

	public static void main(String[] args) {
		String host = (args.length < 1) ? "172.18.0.3" : args[0];
		try (Scanner s = new Scanner(System.in)) {
			Logger.getLogger("").setLevel(Level.OFF);

			System.out.println("obtaining connection factory...");
			com.sun.messaging.ConnectionFactory connectionFactory = new com.sun.messaging.ConnectionFactory();
			connectionFactory.setProperty(ConnectionConfiguration.imqAddressList, host + ":7676");
			connectionFactory.setProperty(ConnectionConfiguration.imqConsumerFlowLimitPrefetch, "false");

			System.out.println("obtained connection factory.");

			System.out.println("obtaining topic...");
			Topic topic = new com.sun.messaging.Topic("PhysicalTopic");

			System.out.println("resolved topic.");

			JMSContext context = connectionFactory.createContext();

			JMSProducer producer = context.createProducer();
			JMSConsumer consumer = context.createConsumer(topic, null, false);
			MessageListener chat = new Chat();
			consumer.setMessageListener(chat);

			System.out.print("\nenter your message always followed by enter:");

			while (true) {
				String content = s.nextLine();
				TextMessage message = context.createTextMessage();
				message.setText(content);
				producer.send(topic, message);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onMessage(Message m) {
		try {
			if (m instanceof TextMessage) {
				System.out.print("received message: ");
				System.out.println(((TextMessage) m).getText());
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
