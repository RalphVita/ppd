package helloworld_rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class CapitalizeClient {

	    public static void main(String[] args) {
	    	String host = (args.length < 1) ? null : args[0];
	    	try {
	    	    Registry registry = LocateRegistry.getRegistry(host);
	    	    Capitalizer stub = (Capitalizer) registry.lookup("Capitalizer001");
	    	
		        System.out.println("Enter lines of text then Ctrl+D or Ctrl+C to quit");
	            Scanner scanner = new Scanner(System.in);
	            while (scanner.hasNextLine()) {
		            String response=stub.toUpperCase(scanner.nextLine());
	                System.out.println(response);
	            }
	    	} catch (Exception e) {
	    	    System.err.println("Client exception: " + e.toString());
	    	    e.printStackTrace();
	    	}
	    }
}
