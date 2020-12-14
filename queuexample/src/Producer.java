/**
 * Exemplo de uso de JMS para um produtor simples de queue.
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
 *  java -cp .:/diretoriodoglassfish/lib/gf-client.jar Producer
 */

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jms.*;

import com.sun.messaging.ConnectionConfiguration;

public class Producer {
     
	public static void main(String[] args) {
				
		String host = (args.length < 1) ? "127.0.0.1" : args[0];
		
		try (Scanner s = new Scanner(System.in)) {
			Logger.getLogger("").setLevel(Level.SEVERE);

			System.out.println("obtaining connection factory...");
			com.sun.messaging.ConnectionFactory connectionFactory = new com.sun.messaging.ConnectionFactory();
			connectionFactory.setProperty(ConnectionConfiguration.imqAddressList,host+":7676");	
			System.out.println("obtained connection factory.");
			
			System.out.println("obtaining queue...");
			Queue queue = new com.sun.messaging.Queue("PhysicalQueue");
			System.out.println("obtained queue.");

			JMSContext context = connectionFactory.createContext();
			JMSProducer producer = context.createProducer();
			
			while (true)
			{
				System.out.print("enter your message:");
				String content = s.nextLine();		    
				TextMessage message = context.createTextMessage(); 
				message.setText(content);
				producer.send(queue,message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
