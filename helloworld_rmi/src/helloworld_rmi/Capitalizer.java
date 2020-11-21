package helloworld_rmi;

public interface Capitalizer extends java.rmi.Remote { 
	String toUpperCase(String s) 
			throws java.rmi.RemoteException;
} 

