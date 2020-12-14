/**
 * Exemplo de uso de JMS para um consumidor simples de queue.
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
 *  java -cp .:/diretoriodoglassfish/lib/gf-client.jar Consumer
 */

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jms.*;

import com.sun.messaging.ConnectionConfiguration;

public class Consumer {
     
	public static void main(String[] args) {
				
		String host = (args.length < 1) ? "127.0.0.1" : args[0];
		
		try {
			Logger.getLogger("").setLevel(Level.INFO);
			
			System.out.println("obtaining connection factory...");
			com.sun.messaging.ConnectionFactory connectionFactory = new com.sun.messaging.ConnectionFactory();
			connectionFactory.setProperty(ConnectionConfiguration.imqAddressList,host+":7676");	
			// esta configuração pode ser importante para evitar que o middleware "puxe" (faça o "prefetch")
			// de várias mensagens
			connectionFactory.setProperty(ConnectionConfiguration.imqConsumerFlowLimitPrefetch,"false");	
			
			System.out.println("obtained connection factory.");
			
			System.out.println("obtaining queue...");
			Queue queue = new com.sun.messaging.Queue("PhysicalQueue");
			System.out.println("obtained queue.");			
	
			JMSContext context = connectionFactory.createContext();
			
			JMSConsumer consumer = context.createConsumer(queue); 

			while (true)
			{
				Message m = consumer.receive();
				if (m instanceof TextMessage)
				{
					System.out.print("\nreceived message: ");
					System.out.println(((TextMessage)m).getText());
				}
				
				Thread.sleep(1000);
				System.out.print("\nidle ");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
