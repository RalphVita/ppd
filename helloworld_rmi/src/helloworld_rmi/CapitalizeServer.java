package helloworld_rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class CapitalizeServer implements Capitalizer {

	@Override
	public String toUpperCase(String s) throws RemoteException {
		System.out.println("recebido pedido com string=["+s+"]");
		return s.toUpperCase();
	}      
	
	/**
	 * No laboratório deve-se executar o CapitalizeServer com opção 
	 * java.rmi.server.hostname setada, da seguinte forma:
	 * 
	 * java -Djava.rmi.server.hostname=IP helloworld_rmi.CapitalizeServer 
	 * 
	 * onde IP é o endereço IP da máquina servidora
	 * 
	 * @param args
	 */
 	public static void main(String args[]) {
	try {
	    CapitalizeServer obj = new CapitalizeServer();
	    Capitalizer objref = (Capitalizer) UnicastRemoteObject.exportObject(obj,0);
	    // Bind the remote object in the registry
	    Registry registry = LocateRegistry.getRegistry("localhost"); // opcional: host
		registry.rebind("Capitalizer001", objref);
	    System.err.println("Server ready");
	} catch (Exception e) {
	    System.err.println("Server exception: " + e.toString());  e.printStackTrace();
	}      }
 	

}
