package br.inf.ufes.ppd.Crack.Server;

import br.inf.ufes.ppd.Master;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class MestreServer {
    public static void main(String[] args) {
        try {
            Mestre obj = new Mestre();

            Master objref = (Master) UnicastRemoteObject.exportObject(obj,0);
            // Bind the remote object in the registry
            Registry registry = LocateRegistry.getRegistry("localhost"); // opcional: host
            registry.rebind("metre", objref);
            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());  e.printStackTrace();
        }
    }
}
