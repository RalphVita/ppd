package br.inf.ufes.ppd.Crack.Client;

import br.inf.ufes.ppd.Master;
import br.inf.ufes.ppd.Slave;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class EscravoClient {
    public static void main(String[] args) {

        String host = (args.length < 1) ? null : args[0];

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            Master master = (Master) registry.lookup("mestre");

            Escravo obj = new Escravo();
            Slave objref = (Slave) UnicastRemoteObject.exportObject(obj,0);



            Scanner s = new Scanner(System.in);

            System.out.println("Seu nome: ");
            String nome = s.nextLine();

            master.addSlave(objref, nome, java.util.UUID.randomUUID());

            /*while (true)
            {
                String content = s.nextLine();
                room.postMessage(new Message(content, sender));
            }*/
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
