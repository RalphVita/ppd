package br.inf.ufes.ppd.Crack.Server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public abstract class Registrator<I, T extends I> {
    public  void Registrar(String name) {
        try {
            T obj = new T();

            I objref = (I) UnicastRemoteObject.exportObject(obj,0);
            // Bind the remote object in the registry
            Registry registry = LocateRegistry.getRegistry("localhost"); // opcional: host
            registry.rebind( "ChatRoom", objref);
            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());  e.printStackTrace();
        }
    }

}

