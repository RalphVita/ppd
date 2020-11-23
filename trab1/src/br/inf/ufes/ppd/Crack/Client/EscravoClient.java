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


            Scanner s = new Scanner(System.in);

            System.out.println("Seu nome: ");
            String nome = s.nextLine();

            Escravo obj = new Escravo(nome);
            obj.BaterPonto();



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
