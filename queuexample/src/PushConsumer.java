/**
 * Exemplo de uso de JMS para um consumidor simples de queue (estilo push).
 * 
 * Instalar Glassfish, rodar:
 * asadmin start-domain 
 * E criar os recursos JMS rodando:
 * asadmin add-resources glassfish-resources.xml
 * (o arquivo XML encontra-se nesse projeto)
 * Verificar o resultado com um browser em: http://localhost:4848/   (Glassfish admin console)
 * 
 * Para rodar, incluir gf-client.jar no classpath:
 * 
 *  java -cp .:/diretoriodoglassfish/lib/gf-client.jar PushConsumer
 */

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jms.*;

import com.sun.messaging.ConnectionConfiguration;

public class PushConsumer implements MessageListener {
     
	public static void main(String[] args) {
				
		String host = (args.length < 1) ? "127.0.0.1" : args[0];
		
		try {
			Logger.getLogger("").setLevel(Level.SEVERE);
			
			System.out.println("obtaining connection factory...");
			com.sun.messaging.ConnectionFactory connectionFactory = new com.sun.messaging.ConnectionFactory();
			connectionFactory.setProperty(ConnectionConfiguration.imqAddressList,host+":7676");	
			System.out.println("obtained connection factory.");
			
			System.out.println("obtaining queue...");
			Queue queue = new com.sun.messaging.Queue("PhysicalQueue");
			System.out.println("obtained queue.");
			
			JMSContext context = connectionFactory.createContext();
			
			JMSConsumer consumer = context.createConsumer(queue); 

			MessageListener listener = new PushConsumer();
			consumer.setMessageListener(listener); 
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onMessage(Message m) {
		try {
			if (m instanceof TextMessage)
			{
				System.out.print("\nreceived message: ");
				System.out.println(((TextMessage)m).getText());
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
